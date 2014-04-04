package com.shrinfo.ibs.task.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.task.dao.TaskDao;

/**
 * 
 * @author Rahul
 * 
 */

public class ReferralTaskServiceImpl extends BaseService implements ReferralTaskService {

    TaskDao taskDao;

    @Override
    public Object invokeMethod(String methodName, Object... args) {
        Object returnValue = null;

        if ("getTasks".equals(methodName)) {
            returnValue = getTasks((BaseVO) args[0]);
        }
        if ("getTask".equals(methodName)) {
            returnValue = getTask((BaseVO) args[0]);
        }
        if ("createTask".equals(methodName)) {
            returnValue = createTask((BaseVO) args[0]);
        }
        return returnValue;
    }

    @Override
    public BaseVO getTasks(BaseVO userVO) {
        return taskDao.getTasks(userVO);
    }

    @Override
    public BaseVO getTask(BaseVO baseVO) {
        return taskDao.getTask(baseVO);
    }

    @Override
    public BaseVO createTask(BaseVO baseVO) {
        return taskDao.createTask(baseVO);
    }


    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

}
