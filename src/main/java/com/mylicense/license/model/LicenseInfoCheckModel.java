package com.mylicense.license.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class LicenseInfoCheckModel implements Serializable {

    private static final long serialVersionUID = -31983343976980894L;

    /**
     * 允许的MAC地址
     */
    private List<String> macAddress;

    /**
     * 允许的IP地址
     */
    private List<String> ipAddress;

    /**
     * 可被允许的CPU序列号
     */
    private String cpuSerial;

    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;
}