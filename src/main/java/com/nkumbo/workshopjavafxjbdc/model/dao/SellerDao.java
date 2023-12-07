package com.nkumbo.workshopjavafxjbdc.model.dao;


import com.nkumbo.workshopjavafxjbdc.model.entities.Department;
import com.nkumbo.workshopjavafxjbdc.model.entities.Seller;

import java.util.List;

public interface SellerDao {

    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Integer id);
    Seller findById(Integer id);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
