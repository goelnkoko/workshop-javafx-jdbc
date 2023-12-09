package com.nkumbo.workshopjavafxjbdc.model.services;

import com.nkumbo.workshopjavafxjbdc.model.dao.DaoFactory;
import com.nkumbo.workshopjavafxjbdc.model.dao.SellerDao;
import com.nkumbo.workshopjavafxjbdc.model.entities.Seller;

import java.util.List;

public class SellerService {

    private SellerDao dao = DaoFactory.createSellerDao();

    public List<Seller> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public Seller findById(Integer id){
        return dao.findById(id);
    }

    public void remove(Seller obj){
        dao.deleteById(obj.getId());
    }
}
