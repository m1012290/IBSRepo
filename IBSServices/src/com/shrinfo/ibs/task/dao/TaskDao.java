package com.shrinfo.ibs.task.dao;

import com.shrinfo.ibs.cmn.vo.BaseVO;


public interface TaskDao {

    public BaseVO getTasks(BaseVO userVO);

    public BaseVO getTask(BaseVO baseVO);

    public BaseVO createTask(BaseVO baseVO);
}
