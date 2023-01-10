package com.example.express.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.express.bean.ResponseBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.Date;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final static int gbunit = 1024 * 1024 * 1024;

    private String res = null;

    private void add(String str) {
        res += str;
    }

    private void addln(String str) {
        res += str + "<br/>";
    }

    private void getDiskInfo() {
        addln("硬盘使用状态：");
        File[] disks = File.listRoots();
        for (File disk : disks) {
            double free = 1.0 * disk.getFreeSpace() / gbunit;
            double total = 1.0 * disk.getTotalSpace() / gbunit;
            double used = total - free;
            add(disk.getPath() + " ");// 如C:\
            add("未使用" + String.format("%.2f", free) + "GB ");
            add("已使用" + String.format("%.2f", used) + "GB ");
            addln("总容量" + String.format("%.2f", total) + "GB");
        }
    }

    private void getMemInfo() {
        addln("分配给服务器的内存信息：");
        add("空闲内存大小:" + String.format("%.2f", 1.0 * Runtime.getRuntime().freeMemory() / gbunit)
                + "GB ");
        addln("总内存大小:" + String.format("%.2f", 1.0 * Runtime.getRuntime().totalMemory() / gbunit)
                + "GB");
    }

    private void getSysMemInfo() {
        addln("服务器总内存信息：");
        boolean ibmVendor = System.getProperty("java.vendor").contains("IBM");
        int totalMb = 0;
        int freeMb = 0;
        Class<?> beanClass;
        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
        try {

            if (ibmVendor) {
                beanClass = Class.forName("com.ibm.lang.management.OperatingSystemMXBean");
            } else {
                beanClass = Class.forName("com.sun.management.OperatingSystemMXBean");
            }
            Method getTotalPhysicalMemorySize = beanClass
                    .getDeclaredMethod("getTotalPhysicalMemorySize");
            Method getFreePhysicalMemorySize = beanClass
                    .getDeclaredMethod("getFreePhysicalMemorySize");
            totalMb = (int) (Long.valueOf(getTotalPhysicalMemorySize.invoke(bean).toString()) / 1024
                    / 1024);
            freeMb = (int) (Long.valueOf(getFreePhysicalMemorySize.invoke(bean).toString()) / 1024
                    / 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        add(String.format("总内存：%dMB ", totalMb));
        add(String.format("剩余内存：%dMB ", freeMb));

        int cores = Runtime.getRuntime().availableProcessors();
        addln(String.format("总核数：%d", cores));
    }

    public void printNow() {
        Date now = new Date();
        addln(String.format("现在时间是: %tY年%tm月%td日 %tH:%tM:%tS", now, now, now, now, now, now));
    }

    @GetMapping("/state")
    public ResponseBean<String> printSysInfo() {
        res = "";
        printNow();
        addln("服务器运行状态：");
        getDiskInfo();
        getMemInfo();
        getSysMemInfo();
        return ResponseBean.success(res);
    }
}
