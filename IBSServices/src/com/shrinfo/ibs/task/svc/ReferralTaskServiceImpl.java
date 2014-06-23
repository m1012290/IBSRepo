package com.shrinfo.ibs.task.svc;

import com.shrinfo.ibs.base.service.BaseService;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.enquiry.dao.EnquiryDao;
import com.shrinfo.ibs.task.dao.TaskDao;
import com.shrinfo.ibs.userdets.dao.UserDetailsDao;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.TaskItemsVO;
import com.shrinfo.ibs.vo.business.TaskVO;

/**
 * 
 * @author Rahul
 * 
 */

public class ReferralTaskServiceImpl extends BaseService implements ReferralTaskService {

    TaskDao taskDao;

    EnquiryDao enquiryDao;
    
    UserDetailsDao userDetailsDao; 

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
        TaskItemsVO taskItemsVO = (TaskItemsVO) taskDao.getTasks(userVO);
        if (!Utils.isEmpty(taskItemsVO) && !Utils.isEmpty(taskItemsVO.getTaskVOs())) {
            EnquiryVO enquiryVO = null;
            for (TaskVO task : taskItemsVO.getTaskVOs()) {
                if(!Utils.isEmpty(task.getEnquiry()) && !Utils.isEmpty(task.getEnquiry().getEnquiryNo())) {
                    enquiryVO = new EnquiryVO();
                    enquiryVO.setEnquiryNo(task.getEnquiry().getEnquiryNo());
                    task.setEnquiry((EnquiryVO) enquiryDao.getEnquiryDetail(enquiryVO));
                    task.setDocumentName(task.getEnquiry().getCustomerDetails().getName());
                }                
                task.setAssignerUser((UserVO)userDetailsDao.getUserDetails(task.getAssignerUser()));          
            }
        }
        return taskItemsVO;
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

    
    public void setEnquiryDao(EnquiryDao enquiryDao) {
        this.enquiryDao = enquiryDao;
    }

    
    public void setUserDetailsDao(UserDetailsDao userDetailsDao) {
        this.userDetailsDao = userDetailsDao;
    }   
    

}
