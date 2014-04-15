package com.shrinfo.ibs.mb;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.shrinfo.ibs.vo.business.TaskVO;


public class TaskItemVODataModel extends ListDataModel<TaskVO> implements
        SelectableDataModel<TaskVO> {


    public TaskItemVODataModel() {
        
    }

    public TaskItemVODataModel(List<TaskVO> taskList) {
        super(taskList);
    }



    @Override
    public TaskVO getRowData(String rowKey) {
        List<TaskVO> wrappedData = (List<TaskVO>) getWrappedData();
        List<TaskVO> itemsList = wrappedData;

        for (TaskVO task : itemsList) {
            if (String.valueOf(task.getId()).equals(rowKey))
                return task;
        }

        return null;
    }

    @Override
    public Object getRowKey(TaskVO task) {
        return task.getId();
    }

}
