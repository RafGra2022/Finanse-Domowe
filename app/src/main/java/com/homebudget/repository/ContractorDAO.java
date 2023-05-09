package com.homebudget.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContractorDAO {

    @Insert
    void insert(ContractorEntity contractorEntity);

    @Query("Select * from contractor")
    List<ContractorEntity> findAllContractors();

    @Query("Delete from contractor where id = :id")
    void deleteContractorById(Long id);
}
