package com.nkumbo.workshopjavafxjbdc.model.services;

import com.nkumbo.workshopjavafxjbdc.model.dao.DaoFactory;
import com.nkumbo.workshopjavafxjbdc.model.dao.DepartmentDao;
import com.nkumbo.workshopjavafxjbdc.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public Department findById(Integer id){
        return dao.findById(id);
    }
}
