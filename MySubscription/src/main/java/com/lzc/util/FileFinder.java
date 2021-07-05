package com.lzc.util;

import lombok.Data;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


/**
 * @author Liang Zhancheng
 * @date 2021/6/27 22:29
 * @description
 */
@Data
public class FileFinder {

    /**
     * 要查找的文件目录
     */
    private String path;
    /**
     * 在文件目录下发现文件时的监听器（即一发现文件就会被这个类监听到）
     */
    private List<Consumer<File>> fileFoundListener;
    /**
     * 文件过滤规则
     */
    private List<FileFilter> ignoreFilters;

    private FileFinder() {
    }

    /**
     * 创建一个文件查找器
     *
     * @param path 要查找的文件路径
     * @return FileFinder
     */
    @SuppressWarnings("all")
    public static FileFinder getInstance(String path) throws FileNotFoundException {
        if (path == null || path.trim().equals("")) {
            throw new FileNotFoundException("path不能为空");
        }
        FileFinder fileFinder = new FileFinder();
        fileFinder.setPath(path);

        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("文件路径不存在:" + path);
        }
        if (!file.isDirectory()) {
            throw new FileFinderException("该路径不是文件夹:" + path);
        }
        if (file.listFiles() == null || file.listFiles().length == 0) {
            throw new FileFinderException("是空文件夹:" + path);
        }

        return fileFinder;
    }

    /**
     * 执行查找
     */
    public List<File> doFind() {
        File file = new File(path);

        File[] files = file.listFiles();
        assert files != null;

        return this.recursiveScanFile(Arrays.asList(files));
    }

    /**
     * 递归扫描文件夹,得到最深处的文件
     *
     * @param files 文件夹
     * @return List<File> 文件夹下的所有文件
     */
    private List<File> recursiveScanFile(List<File> files) {
        List<File> deepFile = new ArrayList<>(files.size());
        for (File file : files) {
            if (!this.isIgnore(file)) {
                File[] folder;
                if (file.isDirectory() && (folder = file.listFiles()) != null) {
                    deepFile.addAll(this.recursiveScanFile(Arrays.asList(folder)));
                } else {
                    deepFile.add(file);
                    this.notifyListener(file);
                }
            }
        }
        return deepFile;
    }

    /**
     * 注册一个文件发现监听
     */
    public FileFinder registerFileFountListener(Consumer<File> listener) {
        if (fileFoundListener == null) {
            fileFoundListener = new ArrayList<>();
        }
        fileFoundListener.add(listener);
        return this;
    }

    /**
     * 添加一个文件过滤规则
     */
    public FileFinder addFileIgnore(FileFilter fileFilter) {
        if (ignoreFilters == null) {
            ignoreFilters = new ArrayList<>();
        }
        ignoreFilters.add(fileFilter);
        return this;
    }

    /**
     * 按照注册的文件忽略规则,进行判断是否忽略当前文件
     *
     * @param file 要验证的文件
     * @return true表示此文件应被忽略
     */
    private boolean isIgnore(File file) {
        if (ignoreFilters == null || ignoreFilters.isEmpty()) {
            return false;
        }
        for (FileFilter predicate : ignoreFilters) {
            if (predicate.accept(file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通知监听器,发现文件了
     */
    private void notifyListener(File file) {
        if (fileFoundListener == null || fileFoundListener.isEmpty()) {
            return;
        }
        // TODO 这里可以换成线程池
        // 不能因为监听器而阻塞文件扫描(即不应因扩展业务分支类影响主流程),所以这里启用异步
        fileFoundListener.forEach(listener -> new Thread(() -> listener.accept(file)).start());
    }

    /**
     * 测试
     */
    public static void main(String[] args) throws FileNotFoundException {
        // 这样写的好处在于、足够灵活、可以添加任意监听器来检测扫描过程，可以动态添加过滤规则。
        List<File> list = FileFinder.getInstance("E:\\FileTestDic")
                .registerFileFountListener(f -> System.out.println("[监听器]监听到新文件:" + f.getName() + ",他的父级目录是:" + f.getParent()))
                // 滤除所有.jpg文件
                .addFileIgnore(f -> f.getName().endsWith(".jpg"))
                // 滤除1文件夹下的所有文件
                .addFileIgnore(f -> f.isDirectory() && "1".equals(f.getName()))
                .doFind();

        System.out.println("\n文件扫描完成,下面打印\n");
        for (File file : list) {
            System.out.println(file.getName() + " : " + file.getPath());
        }
    }
}

/**
 * 文件查找器异常
 */
class FileFinderException extends RuntimeException {
    public FileFinderException(String message) {
        super(message);
    }
}