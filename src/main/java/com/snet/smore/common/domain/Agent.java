package com.snet.smore.common.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Agent {
    private String agentType;
    private String agentNm;
    private String useYn;
    private String changeYn;
    private String configDs;
    private Date createDt;
    private Date updateDt;
}
