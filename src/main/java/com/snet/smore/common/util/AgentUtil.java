package com.snet.smore.common.util;

import com.snet.smore.common.domain.Agent;
import com.snet.smore.common.domain.DbInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
public class AgentUtil {
    public static Agent getAgent(String agentType, String agentName) throws Exception {
        Agent agent = new Agent();

        DbInfo dbInfo = new DbInfo();

        dbInfo.setUsername(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.username"));
        dbInfo.setPassword(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.password"));
        dbInfo.setUrl(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.url"));
        dbInfo.setClassName(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.classname"));

        Connection conn;
        synchronized (conn = DbUtil.getConnection(dbInfo)) {

            String query = "SELECT AGENT_TYPE" +
                    "            , AGENT_NM" +
                    "            , USE_YN" +
                    "            , CHANGE_YN" +
                    "            , CONFIG_DS" +
                    "            , CREATE_DT" +
                    "            , UPDATE_DT" +
                    "         FROM SMORE_AGENT" +
                    "        WHERE AGENT_TYPE = ?" +
                    "          AND AGENT_NM = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, agentType);
                pstmt.setString(2, agentName);

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    agent.setAgentType(rs.getString(1));
                    agent.setAgentNm(rs.getString(2));
                    agent.setUseYn(rs.getString(3));
                    agent.setChangeYn(rs.getString(4));
                    agent.setConfigDs(rs.getString(5));
                    agent.setCreateDt(rs.getDate(6));
                    agent.setUpdateDt(rs.getDate(7));
                }

                conn.commit();
            } catch (Exception e) {
                log.error("An error occurred while getting agent info. [{}:{}]", agentType, agentName, e);
            }
        }

        return agent;
    }

    public static void setChangeYn(String agentType, String agentName, String yn) {
        DbInfo dbInfo = new DbInfo();

        dbInfo.setUsername(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.username"));
        dbInfo.setPassword(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.password"));
        dbInfo.setUrl(EnvManager.getProperty(agentType.toLowerCase() + ".framework.db.url"));

        Connection conn;
        synchronized (conn = DbUtil.getConnection(dbInfo)) {

            String query = "UPDATE SMORE_AGENT" +
                    "          SET CHANGE_YN = ?" +
                    "            , UPDATE_DT = CURRENT_TIMESTAMP" +
                    "        WHERE AGENT_TYPE = ?" +
                    "          AND AGENT_NM = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, yn);
                pstmt.setString(2, agentType);
                pstmt.setString(3, agentName);

                int result = pstmt.executeUpdate();

                conn.commit();

                if (result > 0)
                    log.info("Update [CHANGE_YN] value was successfully completed. [{}:{}], [{}]", agentType, agentName, yn);

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    log.error("An error occurred while rolling back transaction.", ex);
                }

                log.error("An error occurred while updating [CHANGE_YN]. [{}:{}]", agentType, agentName, e);
            }

        }
    }
}
