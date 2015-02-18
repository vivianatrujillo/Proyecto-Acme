-- MySQL Script generated by MySQL Workbench
-- 02/11/15 10:55:34
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema Acme
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Acme
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Acme` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `Acme` ;

-- -----------------------------------------------------
-- Table `Acme`.`RH`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`RH` ;

CREATE TABLE IF NOT EXISTS `Acme`.`RH` (
  `idRH` INT NOT NULL,
  `Nombre` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`idRH`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Eps`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Eps` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Eps` (
  `idEps` INT NOT NULL,
  `Nombre_eps` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idEps`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Trabajador`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Trabajador` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Trabajador` (
  `idTrabajador` INT NOT NULL,
  `Turno_trabajador` INT NOT NULL,
  PRIMARY KEY (`idTrabajador`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Genero`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Genero` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Genero` (
  `idGenero` INT NOT NULL,
  `Nombre` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`idGenero`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Paciente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Paciente` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Paciente` (
  `idPaciente` INT NOT NULL,
  `idTrabajador` INT NOT NULL,
  `idEps` INT NOT NULL,
  `idRH` INT NOT NULL,
  `idGenero` INT NOT NULL,
  `Estado_Civil` INT NOT NULL,
  `Nombre_1` VARCHAR(12) NOT NULL,
  `Nombre_2` VARCHAR(12) NOT NULL,
  `Apellido_1` VARCHAR(12) NOT NULL,
  `Apellido_2` VARCHAR(12) NOT NULL,
  `Tel_fijo` INT NOT NULL,
  `E_mail` VARCHAR(25) NOT NULL,
  `Direccion` VARCHAR(15) NOT NULL,
  `Fecha_Nacimiento` DATETIME NOT NULL,
  `Num_celular` INT NOT NULL,
  PRIMARY KEY (`idPaciente`),
  INDEX `fk_Paciente_RH1_idx` (`idRH` ASC),
  INDEX `fk_Paciente_Eps1_idx` (`idEps` ASC),
  INDEX `fk_Paciente_Trabajador1_idx` (`idTrabajador` ASC),
  INDEX `fk_Paciente_Genero1_idx` (`idGenero` ASC),
  CONSTRAINT `fk_Paciente_RH1`
    FOREIGN KEY (`idRH`)
    REFERENCES `Acme`.`RH` (`idRH`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Paciente_Eps1`
    FOREIGN KEY (`idEps`)
    REFERENCES `Acme`.`Eps` (`idEps`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Paciente_Trabajador1`
    FOREIGN KEY (`idTrabajador`)
    REFERENCES `Acme`.`Trabajador` (`idTrabajador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Paciente_Genero1`
    FOREIGN KEY (`idGenero`)
    REFERENCES `Acme`.`Genero` (`idGenero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Referencia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Referencia` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Referencia` (
  `idReferencia` INT NOT NULL,
  `referencia_familiar` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idReferencia`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Cita`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Cita` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Cita` (
  `idCita` INT NOT NULL,
  `idPaciente` INT NOT NULL,
  `fecha` DATE NOT NULL,
  `Hora` TIME NOT NULL,
  `Num_Turno` INT NOT NULL,
  PRIMARY KEY (`idCita`),
  INDEX `fk_Cita_Paciente1_idx` (`idPaciente` ASC),
  CONSTRAINT `fk_Cita_Paciente1`
    FOREIGN KEY (`idPaciente`)
    REFERENCES `Acme`.`Paciente` (`idPaciente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Valoracion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Valoracion` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Valoracion` (
  `idValoracion` INT NOT NULL,
  `nombre` VARCHAR(25) NOT NULL,
  `idPaciente` INT NOT NULL,
  PRIMARY KEY (`idValoracion`),
  INDEX `fk_Valoracion_Paciente1_idx` (`idPaciente` ASC),
  CONSTRAINT `fk_Valoracion_Paciente1`
    FOREIGN KEY (`idPaciente`)
    REFERENCES `Acme`.`Paciente` (`idPaciente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Tratamiento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Tratamiento` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Tratamiento` (
  `idTratamiento` INT NOT NULL,
  `Nombre_tratamiento` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idTratamiento`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Formato de evolucion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Formato de evolucion` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Formato de evolucion` (
  `idFormato_de_evolucion` INT NOT NULL,
  `idTratamiento` INT NOT NULL,
  `Fecha` DATE NOT NULL,
  `Hora` TIME NOT NULL,
  `Localizacion` VARCHAR(15) NOT NULL,
  `Higien_horal` VARCHAR(20) NOT NULL,
  `Cod_cita` INT NOT NULL,
  PRIMARY KEY (`idFormato_de_evolucion`),
  INDEX `fk_Formato de evolucion_Tratamiento1_idx` (`idTratamiento` ASC),
  CONSTRAINT `fk_Formato de evolucion_Tratamiento1`
    FOREIGN KEY (`idTratamiento`)
    REFERENCES `Acme`.`Tratamiento` (`idTratamiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Diagnostico`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Diagnostico` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Diagnostico` (
  `idDiagnostico` INT NOT NULL,
  `idValoracion` INT NOT NULL,
  `Observaciones` VARCHAR(450) NOT NULL,
  PRIMARY KEY (`idDiagnostico`),
  INDEX `fk_Diagnostico_Valoracion1_idx` (`idValoracion` ASC),
  CONSTRAINT `fk_Diagnostico_Valoracion1`
    FOREIGN KEY (`idValoracion`)
    REFERENCES `Acme`.`Valoracion` (`idValoracion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Rol`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Rol` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Rol` (
  `cod_rol` INT NOT NULL,
  `idTrabajador` INT NOT NULL,
  PRIMARY KEY (`cod_rol`),
  CONSTRAINT `fk_Rol_Trabajador1`
    FOREIGN KEY (`idTrabajador`)
    REFERENCES `Acme`.`Trabajador` (`idTrabajador`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Permisos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Permisos` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Permisos` (
  `cod_permiso` INT NOT NULL,
  `nombre` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`cod_permiso`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Enfermedad`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Enfermedad` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Enfermedad` (
  `idEnfermedad` INT NOT NULL,
  `Nombre_enfermedad` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`idEnfermedad`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Paciente_Enfermedad`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Paciente_Enfermedad` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Paciente_Enfermedad` (
  `cod_Paciente_Enfermedadcol` INT NOT NULL,
  `idPaciente` INT NOT NULL,
  PRIMARY KEY (`cod_Paciente_Enfermedadcol`),
  INDEX `fk_Paciente_has_Enfermedad_Paciente1_idx` (`idPaciente` ASC),
  CONSTRAINT `fk_Paciente_has_Enfermedad_Paciente1`
    FOREIGN KEY (`idPaciente`)
    REFERENCES `Acme`.`Paciente` (`idPaciente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Paciente_Referencia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Paciente_Referencia` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Paciente_Referencia` (
  `cod_Paciente_Referencia` VARCHAR(45) NOT NULL,
  `idPaciente` INT NOT NULL,
  `idReferencia` INT NOT NULL,
  PRIMARY KEY (`cod_Paciente_Referencia`),
  INDEX `fk_Paciente_has_Referencia_Referencia1_idx` (`idReferencia` ASC),
  INDEX `fk_Paciente_has_Referencia_Paciente1_idx` (`idPaciente` ASC),
  CONSTRAINT `fk_Paciente_has_Referencia_Paciente1`
    FOREIGN KEY (`idPaciente`)
    REFERENCES `Acme`.`Paciente` (`idPaciente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Paciente_has_Referencia_Referencia1`
    FOREIGN KEY (`idReferencia`)
    REFERENCES `Acme`.`Referencia` (`idReferencia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Rol_has_Permisos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Rol_has_Permisos` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Rol_has_Permisos` (
  `Rol_Permisos` VARCHAR(45) NOT NULL,
  `cod_rol` INT NOT NULL,
  `cod_permiso` INT NOT NULL,
  PRIMARY KEY (`Rol_Permisos`),
  INDEX `fk_Rol_has_Permisos_Permisos1_idx` (`cod_permiso` ASC),
  INDEX `fk_Rol_has_Permisos_Rol1_idx` (`cod_rol` ASC),
  CONSTRAINT `fk_Rol_has_Permisos_Rol1`
    FOREIGN KEY (`cod_rol`)
    REFERENCES `Acme`.`Rol` (`cod_rol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Rol_has_Permisos_Permisos1`
    FOREIGN KEY (`cod_permiso`)
    REFERENCES `Acme`.`Permisos` (`cod_permiso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Paciente_Enfermedad_Enfermedad`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Paciente_Enfermedad_Enfermedad` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Paciente_Enfermedad_Enfermedad` (
  `cod_Paciente_Enfermedad_Enfermedad` INT NOT NULL,
  `cod_Paciente_Enfermedadcol` INT NOT NULL,
  `idEnfermedad` INT NOT NULL,
  PRIMARY KEY (`idEnfermedad`, `cod_Paciente_Enfermedad_Enfermedad`, `cod_Paciente_Enfermedadcol`),
  INDEX `fk_Paciente_Enfermedad_has_Enfermedad_Enfermedad1_idx` (`idEnfermedad` ASC),
  INDEX `fk_Paciente_Enfermedad_has_Enfermedad_Paciente_Enfermedad1_idx` (`cod_Paciente_Enfermedadcol` ASC),
  CONSTRAINT `fk_Paciente_Enfermedad_has_Enfermedad_Paciente_Enfermedad1`
    FOREIGN KEY (`cod_Paciente_Enfermedadcol`)
    REFERENCES `Acme`.`Paciente_Enfermedad` (`cod_Paciente_Enfermedadcol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Paciente_Enfermedad_has_Enfermedad_Enfermedad1`
    FOREIGN KEY (`idEnfermedad`)
    REFERENCES `Acme`.`Enfermedad` (`idEnfermedad`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Acme`.`Valoracion_Tratamiento`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Acme`.`Valoracion_Tratamiento` ;

CREATE TABLE IF NOT EXISTS `Acme`.`Valoracion_Tratamiento` (
  `cod_Valoracion_Tratamientocol` INT NOT NULL,
  `idValoracion` INT NOT NULL,
  `idTratamiento` INT NOT NULL,
  PRIMARY KEY (`cod_Valoracion_Tratamientocol`),
  INDEX `fk_Valoracion_has_Tratamiento_Tratamiento1_idx` (`idTratamiento` ASC),
  INDEX `fk_Valoracion_has_Tratamiento_Valoracion1_idx` (`idValoracion` ASC),
  CONSTRAINT `fk_Valoracion_has_Tratamiento_Valoracion1`
    FOREIGN KEY (`idValoracion`)
    REFERENCES `Acme`.`Valoracion` (`idValoracion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Valoracion_has_Tratamiento_Tratamiento1`
    FOREIGN KEY (`idTratamiento`)
    REFERENCES `Acme`.`Tratamiento` (`idTratamiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;