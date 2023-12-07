package com.nkumbo.workshopjavafxjbdc.model.dao;


import com.nkumbo.workshopjavafxjbdc.model.entities.Department;

import java.util.List;

public interface DepartmentDao {

    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Department findById(Integer id);
    List<Department> findAll();

}
