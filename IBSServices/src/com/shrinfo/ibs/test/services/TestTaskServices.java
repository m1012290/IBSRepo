package com.shrinfo.ibs.test.services;

import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.task.svc.TaskService;
import com.shrinfo.ibs.vo.business.EnquiryVO;
import com.shrinfo.ibs.vo.business.IBSUserVO;
import com.shrinfo.ibs.vo.business.StatusVO;
import com.shrinfo.ibs.vo.business.TaskVO;


public class TestTaskServices {

    public static void main(String[] args) {

        Utils.setAppContext(Utils.loadSpringBeansFactory("config/applicationcontext.xml"));

        TaskService taskService = (TaskService) Utils.getBean("taskSvc");

        TaskVO taskVO = new TaskVO();

        UserVO assigneeUser = new IBSUserVO();
        assigneeUser.setUserId(12345l);
        taskVO.setAssigneeUser(assigneeUser);

        UserVO assignerUser = new IBSUserVO();
        assignerUser.setUserId(123456l);
        taskVO.setAssignerUser(assignerUser);

        taskVO.setDesc("This is policy referral");
        EnquiryVO enquiry = new EnquiryVO();
        enquiry.setEnquiryNo(12345l);
        taskVO.setEnquiry(enquiry);
        taskVO.setIsStatusActive("Y");
        StatusVO statusVO = new StatusVO();
        statusVO.setCode(1);
        taskVO.setStatusVO(statusVO);

        taskService.createTask(taskVO);

        System.out.println(taskService.getTask(taskVO));
        System.out.println("user tasks:" + taskService.getTasks(assigneeUser));

    }

}
