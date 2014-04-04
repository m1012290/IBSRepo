package com.shrinfo.ibs.task.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.cmn.vo.UserVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsTask;
import com.shrinfo.ibs.vo.business.TaskItemsVO;
import com.shrinfo.ibs.vo.business.TaskVO;


public class TaskDaoImpl extends BaseDBDAO implements TaskDao {

    @Override
    public BaseVO getTasks(BaseVO userVO) {

        if (null == userVO || !(userVO instanceof UserVO)
            || Utils.isEmpty(((UserVO) userVO).getUserId())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid User details. Please check the data passed to fetch details");
        }

        TaskItemsVO taskItemsVO = new TaskItemsVO();
        List<TaskVO> taskVOs = new ArrayList<TaskVO>();

        List ibsTaskObjList = null;
        try {
            ibsTaskObjList =
                getHibernateTemplate().find(
                    " from IbsTask ibsTask where ibsTask.assigneeUserId = ?",
                    ((UserVO) userVO).getUserId());
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetTaskDetails", hibernateException,
                "Error while fecthing Task details");
        }

        Iterator it = ibsTaskObjList.iterator();
        TaskVO taskVO = null;
        Object ibsTaskRaw = null;
        while (it.hasNext()) {
            ibsTaskRaw = it.next();
            if (ibsTaskRaw instanceof IbsTask) {
                taskVO = new TaskVO();
                MapperUtil.populateTaskVO(taskVO, (IbsTask) ibsTaskRaw);
                taskVOs.add(taskVO);
            }
        }
        taskItemsVO.setTaskVOs(taskVOs);
        return taskItemsVO;
    }

    @Override
    public BaseVO getTask(BaseVO baseVO) {

        TaskVO taskVO = new TaskVO();

        if (null == baseVO || !(baseVO instanceof TaskVO)
            || Utils.isEmpty(((TaskVO) baseVO).getId())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid task details. Please chack the data passed to fetch details");
        }

        IbsTask ibsTask = null;

        try {
            ibsTask =
                (IbsTask) getHibernateTemplate().find(" from IbsTask ibsTask where ibsTask.id = ?",
                    ((TaskVO) baseVO).getId()).get(0);
        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotGetTaskDetails", hibernateException,
                "Error while fecthing Task details");
        }

        MapperUtil.populateTaskVO(taskVO, ibsTask);

        return taskVO;
    }

    @Override
    public BaseVO createTask(BaseVO baseVO) {
        if (null == baseVO || !(baseVO instanceof TaskVO)) {
            throw new BusinessException("cmn.unknownError", null, "Task details are invalid");
        }

        TaskVO taskVO = (TaskVO) baseVO;

        IbsTask ibsTask = null;

        try {
            ibsTask = DAOUtils.constructIbsTask(taskVO);
            saveOrUpdate(ibsTask);

            taskVO.setId(ibsTask.getId());

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveTaskDetails", hibernateException,
                "Error while saving task data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveTaskDetails", exception,
                "Error while saving Task data");
        }
        return taskVO;
    }

}
