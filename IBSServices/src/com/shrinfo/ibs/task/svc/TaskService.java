package com.shrinfo.ibs.task.svc;

import com.shrinfo.ibs.cmn.vo.BaseVO;

public interface TaskService {

    public BaseVO getTasks(BaseVO userVO);

    public BaseVO getTask(BaseVO baseVO);

    public BaseVO createTask(BaseVO baseVO);

}
