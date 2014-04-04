package com.shrinfo.ibs.vo.business;

import java.util.List;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;


public class TaskItemsVO extends BaseVO {

    /**
     * 
     */
    private static final long serialVersionUID = 7207860456112685868L;

    List<TaskVO> taskVOs;


    public List<TaskVO> getTaskVOs() {
        return taskVOs;
    }


    public void setTaskVOs(List<TaskVO> taskVOs) {
        this.taskVOs = taskVOs;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {

        StringBuffer taskItem = new StringBuffer("Task items: ");
        if (!Utils.isEmpty(this.taskVOs)) {
            for (TaskVO taskVO : this.taskVOs) {
                taskItem.append("\n" + taskVO.toString());
            }
        }
        return taskItem.toString();
    }

}
