package model.service;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        //MOCKs
        List<Department> list = new ArrayList<>();
            return dao.findAll();
    }

    public void saveOrUpdate(Department obj){
        if(obj.getId() ==null){
            dao.insert(obj);
        }else {
            dao.update(obj);
        }
    }

    public void remote(Department obj){
        dao.deleteById(obj.getId());
    }
}
