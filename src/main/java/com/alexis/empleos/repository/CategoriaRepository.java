package com.alexis.empleos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexis.empleos.model.Categoria;
//import org.springframework.data.repository.CrudRepository;

//public interface CategoriaRepository extends CrudRepository<Categoria, Integer> {
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
