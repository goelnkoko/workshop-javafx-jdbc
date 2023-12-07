package com.nkumbo.workshopjavafxjbdc.model.dao;

import com.nkumbo.workshopjavafxjbdc.database.DB;
import com.nkumbo.workshopjavafxjbdc.model.dao.impl.DepartmentDaoJDBC;
import com.nkumbo.workshopjavafxjbdc.model.dao.impl.SellerDaoJDBC;


public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC(DB.getConnectinon());
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentDaoJDBC(DB.getConnectinon());
    }
}
