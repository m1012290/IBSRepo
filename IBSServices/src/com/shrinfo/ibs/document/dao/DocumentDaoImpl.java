package com.shrinfo.ibs.document.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;

import com.shrinfo.ibs.base.dao.BaseDBDAO;
import com.shrinfo.ibs.cmn.exception.BusinessException;
import com.shrinfo.ibs.cmn.exception.SystemException;
import com.shrinfo.ibs.cmn.utils.Utils;
import com.shrinfo.ibs.cmn.vo.BaseVO;
import com.shrinfo.ibs.dao.utils.DAOUtils;
import com.shrinfo.ibs.dao.utils.MapperUtil;
import com.shrinfo.ibs.gen.pojo.IbsDocumentTable;
import com.shrinfo.ibs.vo.business.DocumentListVO;
import com.shrinfo.ibs.vo.business.DocumentVO;


public class DocumentDaoImpl extends BaseDBDAO implements DocumentDao {

    @Override
    public BaseVO saveDocument(BaseVO baseVO) {
        if (Utils.isEmpty(baseVO) || !(baseVO instanceof DocumentListVO)
            || Utils.isEmpty(((DocumentListVO) baseVO).getDocumentVOs())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid document details. Document details cannot be null or empty");
        }
        DocumentListVO docListVO = (DocumentListVO) baseVO;
        List<IbsDocumentTable> ibsDocuments = new ArrayList<IbsDocumentTable>();

        try {
            Iterator<DocumentVO> docVOIt = docListVO.getDocumentVOs().iterator();
            while (docVOIt.hasNext()) {
                ibsDocuments.add(DAOUtils.constructIbsDocumentTable(docVOIt.next()));
            }
            saveOrUpdateAll(ibsDocuments);

            // Set the generated Document IDs back to VOs
            Iterator<DocumentVO> docVOReturnIt = docListVO.getDocumentVOs().iterator();
            int i = 0;
            while (docVOReturnIt.hasNext()) {
                docVOReturnIt.next().setId(ibsDocuments.get(i).getId());
                i++;
            }

        } catch (HibernateException hibernateException) {
            throw new BusinessException("pas.gi.couldNotSaveDocumentDetails", hibernateException,
                "Error while saving document data");
        } catch (Exception exception) {
            throw new SystemException("pas.gi.couldNotSaveDocumentDetails", exception,
                "Error while saving document data");
        }
        return docListVO;
    }

    @Override
    public BaseVO getDocument(BaseVO baseVO) {

        if (Utils.isEmpty(baseVO) || !(baseVO instanceof DocumentVO)
            || Utils.isEmpty(((DocumentVO) baseVO).getId())) {
            throw new BusinessException("cmn.unknownError", null,
                "Invalid document details. Inavlid data passed to fetch document details");
        }
        DocumentVO documentVO = new DocumentVO();
        IbsDocumentTable ibsDocument = null;
        try {
            ibsDocument =
                (IbsDocumentTable) getHibernateTemplate().find(
                    " from IbsDocumentTable ibsDocument where ibsDocument.id = ?",
                    ((DocumentVO) baseVO).getId()).get(0);
            // documentVO.setDocument(MapperUtil.toByteArray(ibsDocument.getDocument()));
            Blob fileBlob = ibsDocument.getDocument();
            // getHibernateTemplate().refresh(fileBlob);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1000];
            int dataSize;
            InputStream is = fileBlob.getBinaryStream();

            try {
                while ((dataSize = is.read(buf)) != -1) {
                    baos.write(buf, 0, dataSize);
                }
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            documentVO.setDocument(baos.toByteArray());

        } catch (HibernateException hibernateException) {
            throw new BusinessException("", hibernateException,
                "Error while retrieving document details");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            MapperUtil.populateDocumentVO(documentVO, ibsDocument);
        } catch (SQLException e) {
            throw new BusinessException("", e, "Error while populating document VO");
        }

        return documentVO;
    }
}
