package com.example.moduledomain.file.repository;


import com.example.moduledomain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FileRepository extends JpaRepository<File,Long> {

  @Query("select f from File f where f.id in :ids")
  List<File> findFiles(Long[] ids);

}
