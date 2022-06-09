CREATE DATABASE  IF NOT EXISTS `dbproject` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `dbproject`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: dbproject
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `annotazione`
--

DROP TABLE IF EXISTS `annotazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `annotazione` (
  `idAnnotazione` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `DataCreazione` date NOT NULL,
  `Validita` tinyint(4) NOT NULL,
  `Fiducia` varchar(45) NOT NULL,
  `Note` varchar(255) DEFAULT NULL,
  `idImmagine` int(10) unsigned NOT NULL,
  `idUtente` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idAnnotazione`),
  UNIQUE KEY `idAnnotazione_UNIQUE` (`idAnnotazione`),
  KEY `idImmagine_idx` (`idImmagine`),
  KEY `idUtente3_idx` (`idUtente`),
  CONSTRAINT `idImmagine` FOREIGN KEY (`idImmagine`) REFERENCES `immagine` (`idImmagine`),
  CONSTRAINT `idUtente3` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`idUtente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `annotazione`
--

LOCK TABLES `annotazione` WRITE;
/*!40000 ALTER TABLE `annotazione` DISABLE KEYS */;
/*!40000 ALTER TABLE `annotazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campagna`
--

DROP TABLE IF EXISTS `campagna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `campagna` (
  `idCampagna` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Nome` varchar(45) NOT NULL,
  `Committente` varchar(45) NOT NULL,
  `Stato` varchar(45) NOT NULL,
  `idUtente` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idCampagna`),
  UNIQUE KEY `idCampagna_UNIQUE` (`idCampagna`),
  KEY `idUtente_idx` (`idUtente`),
  CONSTRAINT `idUtente` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`idUtente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campagna`
--

LOCK TABLES `campagna` WRITE;
/*!40000 ALTER TABLE `campagna` DISABLE KEYS */;
/*!40000 ALTER TABLE `campagna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `immagine`
--

DROP TABLE IF EXISTS `immagine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `immagine` (
  `idImmagine` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Provenienza` varchar(45) NOT NULL,
  `DataRecupero` varchar(45) NOT NULL,
  `Risoluzione` varchar(45) NOT NULL,
  `url` varchar(45) NOT NULL,
  `idLocalita` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idImmagine`),
  UNIQUE KEY `idImmagine_UNIQUE` (`idImmagine`),
  KEY `idLocalita_idx` (`idLocalita`),
  CONSTRAINT `idLocalita` FOREIGN KEY (`idLocalita`) REFERENCES `localita` (`idLocalita`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `immagine`
--

LOCK TABLES `immagine` WRITE;
/*!40000 ALTER TABLE `immagine` DISABLE KEYS */;
/*!40000 ALTER TABLE `immagine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `localita`
--

DROP TABLE IF EXISTS `localita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `localita` (
  `idLocalita` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Latitudine` double NOT NULL,
  `Longitudine` double NOT NULL,
  `Nome` varchar(45) NOT NULL,
  `Comune` varchar(45) NOT NULL,
  `Regione` varchar(45) NOT NULL,
  `idCampagna` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idLocalita`),
  UNIQUE KEY `idLocalita_UNIQUE` (`idLocalita`),
  KEY `idCampagna_idx` (`idCampagna`),
  CONSTRAINT `idCampagna` FOREIGN KEY (`idCampagna`) REFERENCES `campagna` (`idCampagna`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `localita`
--

LOCK TABLES `localita` WRITE;
/*!40000 ALTER TABLE `localita` DISABLE KEYS */;
/*!40000 ALTER TABLE `localita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partecipazione`
--

DROP TABLE IF EXISTS `partecipazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `partecipazione` (
  `idUtente` int(10) unsigned NOT NULL,
  `idCampagna` int(10) unsigned NOT NULL,
  `idPartecipazione` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idPartecipazione`),
  KEY `idUtente_idx` (`idUtente`),
  KEY `idCampagna_idx` (`idCampagna`),
  CONSTRAINT `idCampagna2` FOREIGN KEY (`idCampagna`) REFERENCES `campagna` (`idCampagna`),
  CONSTRAINT `idUtente2` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`idUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partecipazione`
--

LOCK TABLES `partecipazione` WRITE;
/*!40000 ALTER TABLE `partecipazione` DISABLE KEYS */;
/*!40000 ALTER TABLE `partecipazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `utente` (
  `idUtente` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `Manager` tinyint(4) NOT NULL,
  `Exp` varchar(45) DEFAULT NULL,
  `Foto` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idUtente`),
  UNIQUE KEY `idutente_UNIQUE` (`idUtente`),
  UNIQUE KEY `Username_UNIQUE` (`Username`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'dbproject'
--

--
-- Dumping routines for database 'dbproject'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-09  0:40:49
