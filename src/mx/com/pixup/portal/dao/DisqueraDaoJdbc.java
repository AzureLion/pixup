/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.pixup.portal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import mx.com.pixup.portal.db.DBConecta;
import mx.com.pixup.portal.model.Disquera;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author JAVA-13
 */
public class DisqueraDaoJdbc implements DisqueraDao {
    
    
    public DisqueraDaoJdbc() {       
       
    }

    @Override
    public Disquera insertDisquera(Disquera disquera) {        
        Connection connection = DBConecta.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "insert into disquera (nombre) values (?)";
        try {
            
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, disquera.getNombre());
            preparedStatement.execute();
            
            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            disquera.setId(resultSet.getInt(1));
            
            return disquera;
        } catch (Exception e) {
            Logger.getLogger(DBConecta.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            if(preparedStatement != null) { try{ preparedStatement.close();} catch(Exception e){}}
            if(connection != null) { try{ connection.close();} catch(Exception e){}}
        }
    }

    @Override
    public Disquera updateDisquera(Disquera disquera) {
        String sql = "update disquera set nombre = ? where id = ?";
        Connection connection = DBConecta.getConnection();
        try (
            PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, disquera.getNombre());
            preparedStatement.setInt(2, disquera.getId());
            preparedStatement.execute();
            return disquera;
        } catch (Exception e) {
            return null;
        } 
    }

    @Override
    public void deleteDisquera(Disquera disquera) {       
        Connection connection = DBConecta.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "delete from disquera  where id = ?";
        try {            
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, disquera.getId());
            preparedStatement.execute();
        } catch (Exception e) {
            Logger.getLogger(DBConecta.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if(preparedStatement != null) { try{ preparedStatement.close();} catch(Exception e){}}
            if(connection != null) { try{ connection.close();} catch(Exception e){}}
        }
    }

    @Override
    public List<Disquera> findAllDisqueras() {
        Connection connection = DBConecta.getConnection();
        String sql = "select * from disquera";
        List<Disquera> disqueras = new ArrayList<Disquera>();
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while(resultSet.next()) {                
                Disquera disquera = new Disquera();
                disquera.setId(resultSet.getInt(1));
                disquera.setNombre(resultSet.getString(2));
                disqueras.add(disquera);
            }
        } catch (Exception e) {
            Logger.getLogger(DBConecta.class.getName()).log(Level.SEVERE, null, e);
        } 
        return disqueras;
    }

    @Override
    public Disquera findById(Long idDisquera) {
        Connection connection = DBConecta.getConnection();
        String sql = "select * from disquera where id = " + idDisquera;
        Disquera disquera = new Disquera();
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery();) {
            while(resultSet.next()) {
                disquera.setId(resultSet.getInt("id"));
                disquera.setNombre(resultSet.getString("nombre"));                
            }
        } catch (Exception e) {
            Logger.getLogger(DBConecta.class.getName()).log(Level.SEVERE, null, e);
        }
        return disquera;
    }

    public static void main(String[] args) {
        DisqueraDaoJdbc disqueraDao = new DisqueraDaoJdbc();        
        List<Disquera> listDisqueras = disqueraDao.findAllDisqueras();
        for(Disquera disquera : listDisqueras)
        {
            System.out.println("id: " + disquera.getNombre() );
        }
        
        Disquera d = disqueraDao.findById((long)1);
        
        d.setNombre("daniel rec");
        disqueraDao.updateDisquera(d);
        System.out.println(""+d.getNombre());
    }
    
}
