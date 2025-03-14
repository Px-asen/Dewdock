package top.pxasen.dewdock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 直接链接下载类，用于从网络URL下载文件到本地
 */
public class DirectLinkDownload {
    private static final int BUFFER_SIZE = 8192;
    private String url;
    private String saveDirectory;
    private String fileName;
    private boolean cancelled = false;
    private Consumer<Double> progressCallback;
    private Consumer<File> completionCallback;
    private Consumer<Exception> errorCallback;
    private int connectionTimeout = 10000; // 默认连接超时10秒
    private int readTimeout = 30000; // 默认读取超时30秒
    
    /**
     * 创建下载实例
     * 
     * @param url 下载链接
     * @param saveDirectory 保存目录
     * @param fileName 文件名（如果为null，将从URL中提取）
     */
    public DirectLinkDownload(String url, String saveDirectory, String fileName) {
        this.url = url;
        this.saveDirectory = saveDirectory;
        this.fileName = fileName;
    }
    
    /**
     * 创建下载实例（自动从URL提取文件名）
     * 
     * @param url 下载链接
     * @param saveDirectory 保存目录
     */
    public DirectLinkDownload(String url, String saveDirectory) {
        this(url, saveDirectory, null);
    }
    
    /**
     * 设置进度回调
     * 
     * @param progressCallback 进度回调函数，参数为0.0-1.0的下载进度
     * @return 当前实例，用于链式调用
     */
    public DirectLinkDownload setProgressCallback(Consumer<Double> progressCallback) {
        this.progressCallback = progressCallback;
        return this;
    }
    
    /**
     * 设置完成回调
     * 
     * @param completionCallback 完成回调函数，参数为下载完成的文件
     * @return 当前实例，用于链式调用
     */
    public DirectLinkDownload setCompletionCallback(Consumer<File> completionCallback) {
        this.completionCallback = completionCallback;
        return this;
    }
    
    /**
     * 设置错误回调
     * 
     * @param errorCallback 错误回调函数，参数为发生的异常
     * @return 当前实例，用于链式调用
     */
    public DirectLinkDownload setErrorCallback(Consumer<Exception> errorCallback) {
        this.errorCallback = errorCallback;
        return this;
    }
    
    /**
     * 设置连接超时时间
     * 
     * @param connectionTimeout 连接超时时间（毫秒）
     * @return 当前实例，用于链式调用
     */
    public DirectLinkDownload setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }
    
    /**
     * 设置读取超时时间
     * 
     * @param readTimeout 读取超时时间（毫秒）
     * @return 当前实例，用于链式调用
     */
    public DirectLinkDownload setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
    
    /**
     * 开始下载（同步方法）
     * 
     * @return 下载完成的文件
     * @throws IOException 如果下载过程中发生IO错误
     */
    public File download() throws IOException {
        try {
            return downloadFile();
        } catch (IOException e) {
            if (errorCallback != null) {
                errorCallback.accept(e);
            }
            throw e;
        }
    }
    
    /**
     * 开始下载（异步方法）
     * 
     * @return 包含下载结果的CompletableFuture
     */
    public CompletableFuture<File> downloadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return downloadFile();
            } catch (IOException e) {
                if (errorCallback != null) {
                    errorCallback.accept(e);
                }
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * 取消下载
     */
    public void cancel() {
        this.cancelled = true;
    }
    
    /**
     * 从URL中提取文件名
     * 
     * @param urlString URL字符串
     * @return 提取的文件名
     */
    private String extractFileName(String urlString) {
        try {
            String path = new URL(urlString).getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            return fileName.isEmpty() ? "download" : fileName;
        } catch (Exception e) {
            return "download";
        }
    }
    
    /**
     * 执行文件下载
     * 
     * @return 下载完成的文件
     * @throws IOException 如果下载过程中发生IO错误
     */
    private File downloadFile() throws IOException {
        // 确定文件名
        if (fileName == null || fileName.isEmpty()) {
            fileName = extractFileName(url);
        }
        
        // 确保目录存在
        Path dirPath = Paths.get(saveDirectory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        
        // 创建目标文件
        File outputFile = new File(dirPath.toFile(), fileName);
        
        // 创建URL连接
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        
        // 获取文件大小（如果服务器提供）
        long fileSize = connection.getContentLengthLong();
        
        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(outputFile)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            long totalBytesRead = 0;
            int bytesRead;
            
            while ((bytesRead = in.read(buffer)) != -1) {
                if (cancelled) {
                    // 如果下载被取消，删除部分下载的文件
                    out.close();
                    outputFile.delete();
                    return null;
                }
                
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                
                // 更新进度
                if (progressCallback != null && fileSize > 0) {
                    double progress = (double) totalBytesRead / fileSize;
                    progressCallback.accept(progress);
                }
            }
            
            // 调用完成回调
            if (completionCallback != null) {
                completionCallback.accept(outputFile);
            }
            
            return outputFile;
        }
    }
}
