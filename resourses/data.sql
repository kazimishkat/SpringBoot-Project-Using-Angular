-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: pharmacy
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `branch_inventories`
--

DROP TABLE IF EXISTS `branch_inventories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch_inventories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `quantity_on_hand` int NOT NULL,
  `quantity_reserved` int NOT NULL,
  `batch_id` bigint NOT NULL,
  `branch_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8t1gpgf15ilvt0ukloa0s38wj` (`branch_id`,`batch_id`),
  KEY `FKat9xvjy1sp9pfpgqajgp34d5x` (`batch_id`),
  CONSTRAINT `FKat9xvjy1sp9pfpgqajgp34d5x` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`),
  CONSTRAINT `FKd6t0bdr6kslvnuf3iy3fkj5jx` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch_inventories`
--

LOCK TABLES `branch_inventories` WRITE;
/*!40000 ALTER TABLE `branch_inventories` DISABLE KEYS */;
/*!40000 ALTER TABLE `branch_inventories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS `branches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branches` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `branch_code` varchar(20) NOT NULL,
  `branch_type` enum('BRANCH','CENTRAL_HUB','MAIN','SUB') NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `license_number` varchar(100) DEFAULT NULL,
  `manager_name` varchar(100) DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaqmyw20ht3aku27r3oorfaw43` (`branch_code`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branches`
--

LOCK TABLES `branches` WRITE;
/*!40000 ALTER TABLE `branches` DISABLE KEYS */;
INSERT INTO `branches` VALUES (1,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Tejgaon Industrial Area','Plot # 12, Block B','Dhaka','Bangladesh','1208','Dhaka','BR-001','CENTRAL_HUB','hub.central@pharmacy.com','LIC-DH-2023-001','Kazi Jahangir Alam','Central Distribution Hub','+8801711223301'),(2,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'House # 27, Road # 16 (New)','Dhanmondi','Dhaka','Bangladesh','1209','Dhaka','BR-002','MAIN','dhanmondi.main@pharmacy.com','LIC-DH-2023-002','Tanvir Ahmed Chowdhury','Main Flagship Branch - Dhanmondi','+8801711223302'),(3,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Sector # 3, Sonargaon Janapath','House # 15','Dhaka','Bangladesh','1230','Dhaka','BR-003','BRANCH','uttara@pharmacy.com','LIC-DH-2023-003','Mehedi Hasan','Uttara Model Branch','+8801711223303'),(4,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Gulshan Avenue','Plot # 45, Gulshan-1','Dhaka','Bangladesh','1212','Dhaka','BR-004','BRANCH','gulshan@pharmacy.com','LIC-DH-2023-004','Sabbir Hossain','Gulshan Avenue Branch','+8801711223304'),(5,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Mirpur-10 Circle','Block-C, Section-6','Dhaka','Bangladesh','1216','Dhaka','BR-005','SUB','mirpur.sub@pharmacy.com','LIC-DH-2023-005','Naimur Rahman','Mirpur Sub Branch','+8801711223305'),(6,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'GEC Circle','CDA Avenue','Chattogram','Bangladesh','4000','Chattogram','BR-006','MAIN','ctg.main@pharmacy.com','LIC-CTG-2023-006','Arafat Rahman','Chattogram Main Branch','+8801811223306'),(7,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Agrabad Commercial Area','World Trade Center Road','Chattogram','Bangladesh','4100','Chattogram','BR-007','BRANCH','agrabad@pharmacy.com','LIC-CTG-2023-007','Mahmudul Hasan','Agrabad Commercial Branch','+8801811223307'),(8,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Zindabazar Point','East Zindabazar','Sylhet','Bangladesh','3100','Sylhet','BR-008','BRANCH','sylhet@pharmacy.com','LIC-SYL-2023-008','Kamrul Islam','Sylhet Zindabazar Branch','+8801911223308'),(9,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Shaheb Bazar','Zero Point','Rajshahi','Bangladesh','6000','Rajshahi','BR-009','BRANCH','rajshahi@pharmacy.com','LIC-RAJ-2023-009','Fahim Chowdhury','Rajshahi Town Hall Branch','+8801711223309'),(10,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'KDA Avenue','Royal Junction','Khulna','Bangladesh','9100','Khulna','BR-010','BRANCH','khulna@pharmacy.com','LIC-KHL-2023-010','Shahriar Kabir','Khulna City Branch','+8801811223310'),(11,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Road # 11, Block-E','Banani','Dhaka','Bangladesh','1213','Dhaka','BR-011','SUB','banani.sub@pharmacy.com','LIC-DH-2023-011','Imtiaz Ahmed','Banani Sub Branch','+8801711223311'),(12,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Shadharan Bima Bhaban','Motijheel C/A','Dhaka','Bangladesh','1000','Dhaka','BR-012','BRANCH','motijheel@pharmacy.com','LIC-DH-2023-012','Tariqul Islam','Motijheel Corporate Branch','+8801911223312'),(13,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Sadat Alley','FL Hospital Road','Barishal','Bangladesh','8200','Barishal','BR-013','BRANCH','barishal@pharmacy.com','LIC-BAR-2023-013','Kazi Ashfaq','Barishal Sadar Branch','+8801711223313'),(14,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Station Road','Grand Hotel More','Rangpur','Bangladesh','5400','Rangpur','BR-014','BRANCH','rangpur@pharmacy.com','LIC-RNG-2023-014','Mizanur Rahman','Rangpur Station Road Branch','+8801811223314'),(15,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Kandirpar Circle','Laksam Road','Cumilla','Bangladesh','3500','Chittagong','BR-015','BRANCH','cumilla@pharmacy.com','LIC-CUM-2023-015','Asif Mahmud','Cumilla Kandirpar Branch','+8801911223315'),(16,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Satmatha Square','Jatreshwari Road','Bogra','Bangladesh','5800','Rajshahi','BR-016','BRANCH','bogra@pharmacy.com','LIC-BOG-2023-016','Rashed Khan','Bogra Satmatha Branch','+8801711223316'),(17,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'MK Road','Kotwali','Jashore','Bangladesh','7400','Khulna','BR-017','BRANCH','jashore@pharmacy.com','LIC-JAS-2023-017','Shafiqul Islam','Jashore Town Branch','+8801811223317'),(18,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'College Road','Post Office More','Gopalganj','Bangladesh','8100','Dhaka','BR-018','SUB','gopalganj.sub@pharmacy.com','LIC-GOP-2023-018','Zubair Hossain','Gopalganj Sadar Sub Branch','+8801911223318'),(19,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Chasara More','BB Road','Narayanganj','Bangladesh','1400','Dhaka','BR-019','BRANCH','narayanganj@pharmacy.com','LIC-NAR-2023-019','Hasnat Ali','Narayanganj Chasara Branch','+8801711223319'),(20,'2026-07-21 12:45:00.000000',NULL,_binary '','2026-07-21 12:45:00.000000',NULL,'Chandana Chowrasta','Dhaka-Mymensingh Highway','Gazipur','Bangladesh','1700','Dhaka','BR-020','BRANCH','gazipur@pharmacy.com','LIC-GAZ-2023-020','Anowar Hossain','Gazipur Chowrasta Branch','+8801811223320');
/*!40000 ALTER TABLE `branches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `loyalty_points` int DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKm3iom37efaxd5eucmxjqqcbe9` (`phone`),
  UNIQUE KEY `UKeuat1oase6eqv195jvb71a93s` (`user_id`),
  UNIQUE KEY `UKrfbvkrffamfql7cjmen8v976v` (`email`),
  CONSTRAINT `FKrh1g1a20omjmn6kurd35o3eit` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'2026-07-06 17:57:43.040528',NULL,_binary '','2026-07-06 18:03:40.682553',NULL,'45/A, Dhanmondi 27','Apartment 4B','Dhaka','Bangladesh','1209','Dhaka',28,'gohinshagor123@gmail.com','MALE','Arif_Hossain_a4d17750-b637-417d-abfb-cd8ee74cc29f.jpg',0,'Arif Hossain','$2a$10$sJaJb6fzuyeQVvUF6TLOG.szFSRevdswG42zBl52/QTJ0zRln4v..','0171122334',1),(2,'2026-07-21 11:23:04.534314',NULL,_binary '','2026-07-21 11:23:04.534314',NULL,'dfdf','dfd','dfd','Bangladesh','454','',NULL,'mishkadfd@gmail.com','MALE','mishkat_6cf6f0c8-17d2-4c87-b0b9-8144e39d1518.png',0,'mishkat',NULL,'45454',NULL),(3,'2026-07-21 11:26:22.580747',NULL,_binary '','2026-07-21 11:26:22.580747',NULL,'dfdf','dfd','dfd','Bangladesh','454','',NULL,'mishkatulbd@gmail.com','MALE','ghghg_2ef6ee3b-6599-46b5-8115-33a76cc28b77.png',0,'ghghg',NULL,'656565',19);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_assignments`
--

DROP TABLE IF EXISTS `delivery_assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_assignments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `assigned_at` datetime(6) DEFAULT NULL,
  `delivery_status` varchar(30) DEFAULT NULL,
  `remarks` text,
  `tracking_number` varchar(100) DEFAULT NULL,
  `delivery_company_id` bigint NOT NULL,
  `online_order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsx32wvon481tjt4w54ou1lpo2` (`online_order_id`),
  KEY `FKspuqe0edui418xdmftwmiuwl5` (`delivery_company_id`),
  CONSTRAINT `FKc287yj809h3i212q5inme79sa` FOREIGN KEY (`online_order_id`) REFERENCES `online_orders` (`id`),
  CONSTRAINT `FKspuqe0edui418xdmftwmiuwl5` FOREIGN KEY (`delivery_company_id`) REFERENCES `delivery_companies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_assignments`
--

LOCK TABLES `delivery_assignments` WRITE;
/*!40000 ALTER TABLE `delivery_assignments` DISABLE KEYS */;
/*!40000 ALTER TABLE `delivery_assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_companies`
--

DROP TABLE IF EXISTS `delivery_companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_companies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `api_key` varchar(255) DEFAULT NULL,
  `company_name` varchar(100) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `phone_number` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_companies`
--

LOCK TABLES `delivery_companies` WRITE;
/*!40000 ALTER TABLE `delivery_companies` DISABLE KEYS */;
INSERT INTO `delivery_companies` VALUES (1,'2026-07-15 15:53:59.322384',NULL,_binary '','2026-07-15 15:53:59.322384',NULL,'565','ghghg','ghgh','65656'),(2,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'pth_live_key_9f8d7e6c5b4a3a2','Pathao Courier','Fahim Saleh','+8801711000001'),(3,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'stdfst_live_api_8a7b6c5d4e3f2g1','Steadfast Courier','KM Ridwanul Bari','+8801811000002'),(4,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'pprfly_live_token_7b6a5c4d3e2f1a0','Paperfly','Rahath Ahmed','+8801911000003'),(5,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'redx_api_secret_6c5d4e3f2a1b0c9','RedX Logistics','Afzal Hossain','+8801611000004'),(6,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'scs_partner_key_5d4e3f2a1b0c9d8','Sundarban Courier Service','Sheikh Tanvir','+8801511000005'),(7,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'sap_prod_auth_4e3f2a1b0c9d8e7','SA Parivahan','Matiur Rahman','+8801712000006'),(8,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'ecr_live_app_3f2a1b0c9d8e7f6','eCourier','Biplob Ghosh','+8801812000007'),(9,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'crb_prod_secret_2a1b0c9d8e7f6a5','Carrybee Logistics','Hasnat Tanvir','+8801912000008'),(10,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'chl_api_token_1b0c9d8e7f6a5b4','Chalao Delivery','Kamrul Hasan','+8801612000009'),(11,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'dlg_partner_secret_0c9d8e7f6a5b4c3','Deligram Express','Waheed Hossoin','+8801512000010'),(12,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'bdt_live_key_9d8e7f6a5b4c3d2','Bidyut Express','Shahriar Khan','+8801713000011'),(13,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'jng_prod_token_8e7f6a5b4c3d2e1','Jango Express','Sabbir Ahmed','+8801813000012'),(14,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'zip_partner_key_7e6f5a4b3c2d1e0','Zip Logistics','Mahmudul Islam','+8801913000013'),(15,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'bdc_live_auth_6f5a4b3c2d1e0f9','BD Courier','Asif Chowdhury','+8801613000014'),(16,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'spdx_api_secret_5a4b3c2d1e0f9a8','SpeedX Express','Tariqul Islam','+8801513000015'),(17,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'fst_prod_token_4b3c2d1e0f9a8b7','Fastex Logistics','Imtiaz Ahmed','+8801714000016'),(18,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'pmbd_live_key_3c2d1e0f9a8b7c6','Postmaster BD','Kazi Jahangir','+8801814000017'),(19,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'mvn_partner_secret_2d1e0f9a8b7c6d5','MoveOn Logistics','Mehedi Hasan','+8801914000018'),(20,'2026-07-21 12:05:30.000000',NULL,_binary '\0','2026-07-21 12:05:30.000000',NULL,'xb_prod_auth_1e0f9a8b7c6d5e4','XpressBee BD','Tanvir Rahman','+8801614000019'),(21,'2026-07-21 12:05:30.000000',NULL,_binary '','2026-07-21 12:05:30.000000',NULL,'flr_live_token_0f9a8b7c6d5e4f3','Flyer Express','Naimur Rashid','+8801514000020');
/*!40000 ALTER TABLE `delivery_companies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (1,'java');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `generic_medicines`
--

DROP TABLE IF EXISTS `generic_medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `generic_medicines` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `contraindications` text,
  `description` text,
  `generic_name` varchar(150) NOT NULL,
  `indication` text,
  `side_effects` text,
  `category_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK7ilfokcosejkquvs3bxisy7nq` (`generic_name`),
  KEY `FK9xucqwyct0355iun7ipaddb77` (`category_id`),
  CONSTRAINT `FK9xucqwyct0355iun7ipaddb77` FOREIGN KEY (`category_id`) REFERENCES `medicine_categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `generic_medicines`
--

LOCK TABLES `generic_medicines` WRITE;
/*!40000 ALTER TABLE `generic_medicines` DISABLE KEYS */;
INSERT INTO `generic_medicines` VALUES (1,'2026-07-08 17:56:06.953829',NULL,_binary '','2026-07-08 17:56:06.953829',NULL,'dfdf','fgfgfgf','fgfgfgf','dfdfd','dfdf',1),(2,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Severe hepatic impairment, hypersensitivity.','Common analgesic and antipyretic agent.','Paracetamol','Fever, mild to moderate pain, headache.','Nausea, allergic skin reactions (rare).',2),(3,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Hypersensitivity to substituted benzimidazoles.','Proton pump inhibitor reducing stomach acid.','Omeprazole','GERD, peptic ulcer, acid reflux.','Headache, abdominal pain, diarrhea, flatulence.',3),(4,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','History of penicillin allergy.','Broad-spectrum beta-lactam antibiotic.','Amoxicillin','Bacterial infections of ear, nose, throat, and tract.','Diarrhea, rash, nausea, vomiting.',1),(5,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Severe renal impairment.','Second-generation non-drowsy antihistamine.','Cetirizine','Allergies, rhinitis, chronic urticaria.','Drowsiness, dry mouth, fatigue.',4),(6,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Severe hypotension, cardiogenic shock.','Dihydropyridine calcium channel blocker.','Amlodipine','Hypertension, chronic stable angina.','Peripheral edema, dizziness, flushing, palpitations.',5),(7,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Severe renal dysfunction, acute metabolic acidosis.','Biguanide oral antihyperglycemic agent.','Metformin','Type 2 diabetes mellitus management.','Gastrointestinal upset, metallic taste, lactic acidosis (rare).',6),(8,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Active liver disease, pregnancy, lactation.','HMG-CoA reductase inhibitor (statin).','Atorvastatin','Hypercholesterolemia, cardiovascular prevention.','Myalgia, elevated liver enzymes, constipation.',7),(9,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Hypersensitivity to salbutamol.','Short-acting beta2-adrenergic agonist.','Salbutamol','Bronchospasm in asthma and COPD.','Tremors, tachycardia, headache, muscle cramps.',8),(10,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Hypercalcemia, vitamin D toxicity.','Fat-soluble vitamin essential for bone health.','Cholecalciferol (Vitamin D3)','Vitamin D deficiency, osteoporosis prophylaxis.','Hypercalcemia, hypercalciuria (on excessive overdose).',9),(11,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Systemic fungal infections, active skin infections.','Short-acting topical and systemic corticosteroid.','Hydrocortisone','Skin inflammation, eczema, severe allergic conditions.','Skin atrophy, burning sensation, delayed wound healing.',10),(12,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Cholestatic jaundice/hepatic dysfunction with prior use.','Macrolide antibiotic with long half-life.','Azithromycin','Respiratory tract infections, skin infections, STDs.','Diarrhea, vomiting, abdominal cramps, QT prolongation.',1),(13,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Hypersensitivity to esomeprazole or benzimidazoles.','S-enantiomer of omeprazole with acid suppression.','Esomeprazole','Duodenal ulcers, erosive esophagitis, Zollinger-Ellison.','Headache, flatulence, abdominal pain.',3),(14,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Pregnancy (risk of fetal toxicity), severe hepatic impairment.','Angiotensin II receptor antagonist (ARB).','Losartan','Hypertension, diabetic nephropathy in Type 2 diabetes.','Dizziness, fatigue, hyperkalemia.',5),(15,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Concomitant tizanidine administration, tendon disorders.','Broad-spectrum fluoroquinolone antibacterial.','Ciprofloxacin','Urinary tract infections, typhoid fever, bone infections.','Nausea, tendonitis, tendon rupture, central nervous system effects.',1),(16,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','First trimester of pregnancy (relative), hypersensitivity.','Synthetic nitroimidazole antiprotozoal & antibacterial.','Metronidazole','Amebiasis, giardiasis, anaerobic bacterial infections.','Nausea, metallic taste, dark urine, disulfiram-like reaction with alcohol.',11),(17,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Concomitant administration with terfenadine or cisapride.','Triazole antifungal medication.','Fluconazole','Candidiasis, cryptococcal meningitis, fungal infections.','Headache, rash, abdominal discomfort, elevated liver enzymes.',17),(18,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Severe respiratory insufficiency, sleep apnea, myasthenia gravis.','Long-acting benzodiazepine anxiolytic and muscle relaxant.','Diazepam','Anxiety disorders, muscle spasms, acute seizures.','Sedation, ataxia, memory impairment, dependence.',12),(19,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Hypersensitivity to montelukast.','Leukotriene receptor antagonist (LTRA).','Montelukast','Asthma prophylaxis, allergic rhinitis management.','Headache, abdominal pain, neuropsychiatric events (rare).',8),(20,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Episodes of hypoglycemia, hypersensitivity to human insulin.','Short-acting human insulin recombinant DNA origin.','Insulin Human (Regular)','Glycemic control in Type 1 and Type 2 diabetes.','Hypoglycemia, lipodystrophy at injection site, weight gain.',6),(21,'2026-07-20 16:00:56.000000','SYSTEM',_binary '','2026-07-20 16:00:56.000000','SYSTEM','Active peptic ulcer, severe heart failure, third trimester pregnancy.','Nonsteroidal anti-inflammatory drug (NSAID).','Ibuprofen','Inflammatory pain, rheumatoid arthritis, dysmenorrhea.','Gastric ulceration, GI bleeding, renal impairment, heartburn.',2);
/*!40000 ALTER TABLE `generic_medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_received_note_items`
--

DROP TABLE IF EXISTS `goods_received_note_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_received_note_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `batch_number` varchar(50) NOT NULL,
  `expiry_date` date NOT NULL,
  `manufacture_date` date DEFAULT NULL,
  `purchase_price` decimal(12,2) DEFAULT NULL,
  `received_quantity` int NOT NULL,
  `selling_price` decimal(12,2) DEFAULT NULL,
  `grn_id` bigint NOT NULL,
  `medicine_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5n2os0bcb1ke1vgwjny8vmeh9` (`grn_id`),
  KEY `FKrowv5e4xqudxrga9psu841vmk` (`medicine_id`),
  CONSTRAINT `FK5n2os0bcb1ke1vgwjny8vmeh9` FOREIGN KEY (`grn_id`) REFERENCES `goods_received_notes` (`id`),
  CONSTRAINT `FKrowv5e4xqudxrga9psu841vmk` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_received_note_items`
--

LOCK TABLES `goods_received_note_items` WRITE;
/*!40000 ALTER TABLE `goods_received_note_items` DISABLE KEYS */;
INSERT INTO `goods_received_note_items` VALUES (1,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-SQ-202601','2028-01-09','2026-01-10',12.50,500,15.00,1,1),(2,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-SQ-202602','2027-07-14','2026-01-15',45.00,300,52.00,1,2),(3,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-INC-202601','2028-01-31','2026-02-01',8.00,1000,10.00,2,3),(4,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-INC-202602','2027-08-04','2026-02-05',120.00,250,140.00,2,4),(5,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-BEX-202601','2028-02-09','2026-02-10',22.00,400,26.00,3,5),(6,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-REN-202601','2028-02-28','2026-03-01',15.00,600,18.00,4,6),(7,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-REN-202602','2027-09-04','2026-03-05',85.00,150,100.00,4,7),(8,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-HPL-202601','2028-03-09','2026-03-10',18.50,800,22.00,5,8),(9,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-HPL-202602','2027-09-11','2026-03-12',65.00,300,75.00,5,9),(10,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-ACI-202601','2028-03-31','2026-04-01',30.00,500,35.00,6,10),(11,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-ARI-202601','2028-04-09','2026-04-10',5.50,1000,7.00,7,11),(12,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-ARI-202602','2027-10-14','2026-04-15',150.00,200,175.00,7,12),(13,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-SKF-202601','2028-04-30','2026-05-01',40.00,450,48.00,8,13),(14,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-ACM-202601','2028-05-09','2026-05-10',11.00,700,13.50,9,14),(15,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-DIL-202601','2027-11-14','2026-05-15',95.00,350,110.00,10,15),(16,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-DIL-202602','2028-05-19','2026-05-20',25.00,500,30.00,10,16),(17,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-OPS-202601','2028-05-31','2026-06-01',14.00,600,17.00,11,17),(18,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-POP-202601','2027-12-04','2026-06-05',210.00,200,240.00,12,18),(19,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-RAD-202601','2028-06-09','2026-06-10',9.50,800,12.00,13,19),(20,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-RAD-202602','2027-12-11','2026-06-12',350.00,100,400.00,13,20),(21,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-SQ-202603','2028-06-14','2026-06-15',12.50,400,15.00,14,1),(22,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-INC-202603','2028-06-17','2026-06-18',8.00,500,10.00,15,3),(23,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-BEX-202602','2028-06-19','2026-06-20',22.00,300,26.00,16,5),(24,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-REN-202603','2027-12-21','2026-06-22',85.00,250,100.00,17,7),(25,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-HPL-202603','2028-06-24','2026-06-25',65.00,400,75.00,18,9),(26,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-ARI-202603','2028-06-27','2026-06-28',5.50,1200,7.00,19,11),(27,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'BATCH-SKF-202602','2028-06-30','2026-07-01',40.00,300,48.00,20,13);
/*!40000 ALTER TABLE `goods_received_note_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_received_notes`
--

DROP TABLE IF EXISTS `goods_received_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods_received_notes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `approval_status` enum('APPROVED','CANCELLED','PENDING','REJECTED') DEFAULT NULL,
  `grn_number` varchar(30) NOT NULL,
  `received_date` date NOT NULL,
  `purchase_order_id` bigint NOT NULL,
  `received_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKi0rtwqa3l7l3wy0h07pbgaw6c` (`grn_number`),
  KEY `FK7goex26oj6qofvqap9cns6g2w` (`purchase_order_id`),
  KEY `FKor7g7ihbvc9ewv4kxwk9w7ag6` (`received_by`),
  CONSTRAINT `FK7goex26oj6qofvqap9cns6g2w` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`),
  CONSTRAINT `FKor7g7ihbvc9ewv4kxwk9w7ag6` FOREIGN KEY (`received_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_received_notes`
--

LOCK TABLES `goods_received_notes` WRITE;
/*!40000 ALTER TABLE `goods_received_notes` DISABLE KEYS */;
INSERT INTO `goods_received_notes` VALUES (1,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:19:19.096973',NULL,'APPROVED','GRN-2026-0001','2026-06-03',1,NULL),(2,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:19:30.124853',NULL,'APPROVED','GRN-2026-0002','2026-06-06',2,NULL),(3,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:19:37.512429',NULL,'APPROVED','GRN-2026-0003','2026-06-08',3,NULL),(4,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:19:43.974981',NULL,'APPROVED','GRN-2026-0004','2026-06-12',4,NULL),(5,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:19:53.625671',NULL,'APPROVED','GRN-2026-0005','2026-06-15',5,NULL),(6,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:20:06.666466',NULL,'APPROVED','GRN-2026-0006','2026-06-18',6,NULL),(7,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:20:18.308989',NULL,'APPROVED','GRN-2026-0007','2026-06-22',7,NULL),(8,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:20:53.303233',NULL,'APPROVED','GRN-2026-0008','2026-06-25',8,NULL),(9,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:21:08.024635',NULL,'APPROVED','GRN-2026-0009','2026-06-28',9,NULL),(10,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:21:15.785103',NULL,'APPROVED','GRN-2026-0010','2026-07-03',10,NULL),(11,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0011','2026-07-05',11,NULL),(12,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0012','2026-07-08',12,NULL),(13,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0013','2026-07-10',13,NULL),(14,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0014','2026-07-12',14,NULL),(15,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0015','2026-07-14',15,NULL),(16,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0016','2026-07-16',16,NULL),(17,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0017','2026-07-18',17,NULL),(18,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0018','2026-07-19',18,NULL),(19,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0019','2026-07-20',19,NULL),(20,'2026-07-21 13:18:02.000000',NULL,_binary '','2026-07-21 13:18:02.000000',NULL,'PENDING','GRN-2026-0020','2026-07-21',20,NULL);
/*!40000 ALTER TABLE `goods_received_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_batches`
--

DROP TABLE IF EXISTS `medicine_batches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_batches` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `batch_number` varchar(50) NOT NULL,
  `expiry_date` date NOT NULL,
  `manufacture_date` date DEFAULT NULL,
  `purchase_price` decimal(12,2) DEFAULT NULL,
  `selling_price` decimal(12,2) DEFAULT NULL,
  `medicine_id` bigint NOT NULL,
  `supplier_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc7xcd7bwmfmmfm24oixxu00a2` (`medicine_id`),
  KEY `FKlds0umb8e6lk2afnbqnmjyy41` (`supplier_id`),
  CONSTRAINT `FKc7xcd7bwmfmmfm24oixxu00a2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`),
  CONSTRAINT `FKlds0umb8e6lk2afnbqnmjyy41` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_batches`
--

LOCK TABLES `medicine_batches` WRITE;
/*!40000 ALTER TABLE `medicine_batches` DISABLE KEYS */;
INSERT INTO `medicine_batches` VALUES (1,'2026-07-21 13:19:19.059555',NULL,_binary '','2026-07-21 13:19:19.059555',NULL,'BATCH-SQ-202601','2028-01-09','2026-01-10',12.50,15.00,1,1),(2,'2026-07-21 13:19:19.068533',NULL,_binary '','2026-07-21 13:19:19.068533',NULL,'BATCH-SQ-202602','2027-07-14','2026-01-15',45.00,52.00,2,1),(3,'2026-07-21 13:19:30.077722',NULL,_binary '','2026-07-21 13:19:30.077722',NULL,'BATCH-INC-202601','2028-01-31','2026-02-01',8.00,10.00,3,2),(4,'2026-07-21 13:19:30.096670',NULL,_binary '','2026-07-21 13:19:30.096670',NULL,'BATCH-INC-202602','2027-08-04','2026-02-05',120.00,140.00,4,2),(5,'2026-07-21 13:19:37.496190',NULL,_binary '','2026-07-21 13:19:37.496190',NULL,'BATCH-BEX-202601','2028-02-09','2026-02-10',22.00,26.00,5,3),(6,'2026-07-21 13:19:43.952845',NULL,_binary '','2026-07-21 13:19:43.952845',NULL,'BATCH-REN-202601','2028-02-28','2026-03-01',15.00,18.00,6,4),(7,'2026-07-21 13:19:43.961380',NULL,_binary '','2026-07-21 13:19:43.961380',NULL,'BATCH-REN-202602','2027-09-04','2026-03-05',85.00,100.00,7,4),(8,'2026-07-21 13:19:53.601515',NULL,_binary '','2026-07-21 13:19:53.601515',NULL,'BATCH-HPL-202601','2028-03-09','2026-03-10',18.50,22.00,8,5),(9,'2026-07-21 13:19:53.613138',NULL,_binary '','2026-07-21 13:19:53.613138',NULL,'BATCH-HPL-202602','2027-09-11','2026-03-12',65.00,75.00,9,5),(10,'2026-07-21 13:20:06.655849',NULL,_binary '','2026-07-21 13:20:06.655849',NULL,'BATCH-ACI-202601','2028-03-31','2026-04-01',30.00,35.00,10,6),(11,'2026-07-21 13:20:18.282413',NULL,_binary '','2026-07-21 13:20:18.282413',NULL,'BATCH-ARI-202601','2028-04-09','2026-04-10',5.50,7.00,11,7),(12,'2026-07-21 13:20:18.291018',NULL,_binary '','2026-07-21 13:20:18.291018',NULL,'BATCH-ARI-202602','2027-10-14','2026-04-15',150.00,175.00,12,7),(13,'2026-07-21 13:20:53.293269',NULL,_binary '','2026-07-21 13:20:53.293269',NULL,'BATCH-SKF-202601','2028-04-30','2026-05-01',40.00,48.00,13,8),(14,'2026-07-21 13:21:08.010670',NULL,_binary '','2026-07-21 13:21:08.010670',NULL,'BATCH-ACM-202601','2028-05-09','2026-05-10',11.00,13.50,14,9),(15,'2026-07-21 13:21:15.765661',NULL,_binary '','2026-07-21 13:21:15.765661',NULL,'BATCH-DIL-202601','2027-11-14','2026-05-15',95.00,110.00,15,10),(16,'2026-07-21 13:21:15.769834',NULL,_binary '','2026-07-21 13:21:15.769834',NULL,'BATCH-DIL-202602','2028-05-19','2026-05-20',25.00,30.00,16,10);
/*!40000 ALTER TABLE `medicine_batches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_categories`
--

DROP TABLE IF EXISTS `medicine_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `description` text,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4y8k76cpx76u2arfvndhe5iwc` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_categories`
--

LOCK TABLES `medicine_categories` WRITE;
/*!40000 ALTER TABLE `medicine_categories` DISABLE KEYS */;
INSERT INTO `medicine_categories` VALUES (1,'2026-07-08 16:48:17.063800',NULL,_binary '','2026-07-08 16:48:17.063800',NULL,'sdsds','hfdafh'),(2,'2026-07-12 18:58:52.588416',NULL,_binary '','2026-07-12 18:58:52.588416',NULL,'sdsdsdsd','sdsds'),(3,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Medicines used to treat bacterial infections','Antibiotics'),(4,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Pain relievers and fever reducers','Analgesics & Antipyretics'),(5,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Medications for acidity, heartburn, and peptic ulcers','Antacids & Antiulcerants'),(6,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Used for allergic reactions, cold, and seasonal allergies','Antihistamines'),(7,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Blood pressure management medications','Antihypertensives'),(8,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Oral drugs and insulins for blood sugar control','Antidiabetics'),(9,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Medications for heart conditions and circulation','Cardiovascular Drugs'),(10,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Bronchodilators, inhalers, and asthma medications','Respiratory & Anti-asthmatics'),(11,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Multivitamins, minerals, and dietary supplements','Vitamins & Supplements'),(12,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Topical ointments, creams, and skin care treatments','Dermatologicals'),(13,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Antidiarrheals, laxatives, and digestive aids','Gastrointestinal Drugs'),(14,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Antidepressants, sedatives, and nerve medications','Neurological & Psychiatric'),(15,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Eye drops, ear drops, and related ointments','Ophthalmic & Otic Supplies'),(16,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Medications formatted specifically for children','Pediatric Formulations'),(17,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Hormonal treatments, contraceptives, and women health care','Gynecological Drugs'),(18,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Kidney and urinary tract infection treatments','Urological Drugs'),(19,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Oral and topical treatments for fungal infections','Antifungals'),(20,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Medications specifically targeting viral infections','Antivirals'),(21,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Corticosteroids and anti-inflammatory drugs','Steroids & Anti-inflammatories'),(22,'2026-07-20 15:58:59.000000','SYSTEM',_binary '','2026-07-20 15:58:59.000000','SYSTEM','Immunization vaccines and biological products','Vaccines & Biologicals');
/*!40000 ALTER TABLE `medicine_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `brand_name` varchar(150) NOT NULL,
  `default_purchase_price` decimal(12,2) DEFAULT NULL,
  `default_selling_price` decimal(12,2) DEFAULT NULL,
  `description` text,
  `dosage_form` enum('CAPSULE','CREAM','DROPS','GEL','INHALER','INJECTION','LOTION','OINTMENT','PATCH','POWDER','SACHET','SPRAY','SUPPOSITORY','SUSPENSION','SYRUP','TABLET') DEFAULT NULL,
  `drug_schedule` enum('CONTROLLED_SUBSTANCE','NARCOTIC','OTC','PRESCRIPTION_REQUIRED') DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(150) DEFAULT NULL,
  `medicine_code` varchar(30) NOT NULL,
  `reorder_level` int DEFAULT NULL,
  `reorder_quantity` int DEFAULT NULL,
  `storage_condition` enum('COLD_CHAIN','FROZEN','PROTECT_FROM_LIGHT','PROTECT_FROM_MOISTURE','REFRIGERATED','ROOM_TEMPERATURE') DEFAULT NULL,
  `strength` varchar(50) DEFAULT NULL,
  `unit_of_measure` enum('AMPOULE','BOTTLE','BOX','JAR','PIECE','SACHET','STRIP','TUBE','VIAL') DEFAULT NULL,
  `units_per_pack` int DEFAULT NULL,
  `vat_percentage` decimal(5,2) DEFAULT NULL,
  `generic_medicine_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKld4htm04xvhtv2rfn0motojql` (`medicine_code`),
  KEY `FKgwlp43x4866mnkixp61dv8q2a` (`generic_medicine_id`),
  CONSTRAINT `FKgwlp43x4866mnkixp61dv8q2a` FOREIGN KEY (`generic_medicine_id`) REFERENCES `generic_medicines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Napa Extra',2.00,2.50,'Used for fever and pain relief.','TABLET','OTC',NULL,'Square Pharmaceuticals','MED-001',100,500,'ROOM_TEMPERATURE','500mg+65mg','STRIP',10,5.00,1),(2,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Seclo 20',4.50,6.00,'Proton pump inhibitor for hyperacidity.','CAPSULE','PRESCRIPTION_REQUIRED',NULL,'Square Pharmaceuticals','MED-002',50,200,'ROOM_TEMPERATURE','20mg','BOX',60,5.00,2),(3,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Aramox 500',6.00,7.50,'Broad-spectrum antibiotic for infections.','CAPSULE','PRESCRIPTION_REQUIRED',NULL,'Incepta Pharmaceuticals','MED-003',30,100,'ROOM_TEMPERATURE','500mg','STRIP',10,5.00,3),(4,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Alatrol 10',2.50,3.00,'Antihistamine for seasonal allergies.','TABLET','OTC',NULL,'Square Pharmaceuticals','MED-004',40,150,'ROOM_TEMPERATURE','10mg','STRIP',10,5.00,4),(5,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Camlodin 5',4.00,5.00,'Calcium channel blocker for hypertension.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Square Pharmaceuticals','MED-005',50,200,'ROOM_TEMPERATURE','5mg','STRIP',10,5.00,5),(6,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Metfo 500',1.50,2.00,'Oral blood sugar management medicine.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Beximco Pharmaceuticals','MED-006',60,250,'ROOM_TEMPERATURE','500mg','STRIP',10,5.00,6),(7,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Atova 10',8.00,10.00,'Statin for lowering cholesterol.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Incepta Pharmaceuticals','MED-007',30,120,'ROOM_TEMPERATURE','10mg','STRIP',10,5.00,7),(8,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Windel 100ml',35.00,42.00,'Bronchodilator syrup for asthma and cough.','SYRUP','OTC',NULL,'ACI Limited','MED-008',20,50,'ROOM_TEMPERATURE','2mg/5ml','BOTTLE',1,5.00,8),(9,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','D-Rise 20000',12.00,15.00,'High potency Vitamin D3 capsule.','CAPSULE','PRESCRIPTION_REQUIRED',NULL,'Incepta Pharmaceuticals','MED-009',25,80,'ROOM_TEMPERATURE','20000 IU','STRIP',10,5.00,9),(10,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Dermasol 10g',45.00,55.00,'Topical steroid cream for skin inflammation.','CREAM','OTC',NULL,'Square Pharmaceuticals','MED-010',15,40,'ROOM_TEMPERATURE','0.05%','TUBE',1,5.00,10),(11,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Zimax 500',30.00,35.00,'Macrolide antibiotic for respiratory tract.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Incepta Pharmaceuticals','MED-011',20,100,'ROOM_TEMPERATURE','500mg','BOX',18,5.00,11),(12,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Sergel 20',6.00,7.00,'Delayed-release acid reflux treatment.','CAPSULE','PRESCRIPTION_REQUIRED',NULL,'Healthcare Pharmaceuticals','MED-012',40,150,'ROOM_TEMPERATURE','20mg','BOX',30,5.00,12),(13,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Osartil 50',7.00,8.50,'Angiotensin II blocker for blood pressure.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Incepta Pharmaceuticals','MED-013',30,100,'ROOM_TEMPERATURE','50mg','STRIP',10,5.00,13),(14,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Ciprocin 500',12.00,15.00,'Fluoroquinolone antibiotic for general infections.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Square Pharmaceuticals','MED-014',30,100,'ROOM_TEMPERATURE','500mg','STRIP',10,5.00,14),(15,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Flagyl 400',1.80,2.20,'Antibacterial and antiprotozoal tablet.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Sanofi Bangladesh','MED-015',50,200,'ROOM_TEMPERATURE','400mg','STRIP',10,5.00,15),(16,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Omastat 150',25.00,30.00,'Oral antifungal capsule for fungal infections.','CAPSULE','PRESCRIPTION_REQUIRED',NULL,'Beximco Pharmaceuticals','MED-016',20,60,'ROOM_TEMPERATURE','150mg','STRIP',10,5.00,16),(17,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Sedil 5',1.00,1.50,'Anxiolytic tablet for anxiety and relaxation.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Square Pharmaceuticals','MED-017',10,30,'ROOM_TEMPERATURE','5mg','STRIP',10,5.00,17),(18,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Monas 10',13.00,16.00,'Leukotriene inhibitor for chronic asthma.','TABLET','PRESCRIPTION_REQUIRED',NULL,'Acme Laboratories','MED-018',40,120,'ROOM_TEMPERATURE','10mg','BOX',30,5.00,18),(19,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Humulin R 100IU',420.00,480.00,'Short-acting insulin for diabetes control.','INJECTION','PRESCRIPTION_REQUIRED',NULL,'Eli Lilly','MED-019',10,30,'REFRIGERATED','100IU/ml','VIAL',1,5.00,19),(20,'2026-07-21 12:21:44.000000','SYSTEM',_binary '','2026-07-21 12:21:44.000000','SYSTEM','Ace 500',1.00,1.20,'Fast pain reliever and antipyretic.','TABLET','OTC',NULL,'Square Pharmaceuticals','MED-020',100,500,'ROOM_TEMPERATURE','500mg','STRIP',10,5.00,20);
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `online_order_items`
--

DROP TABLE IF EXISTS `online_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `online_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `price_per_unit` double NOT NULL,
  `quantity` int NOT NULL,
  `medicine_id` bigint NOT NULL,
  `online_order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhr9qjyqa4ass6vfrnk4muovqk` (`medicine_id`),
  KEY `FK1bbvqj0slic8qopawxtcnlgy` (`online_order_id`),
  CONSTRAINT `FK1bbvqj0slic8qopawxtcnlgy` FOREIGN KEY (`online_order_id`) REFERENCES `online_orders` (`id`),
  CONSTRAINT `FKhr9qjyqa4ass6vfrnk4muovqk` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `online_order_items`
--

LOCK TABLES `online_order_items` WRITE;
/*!40000 ALTER TABLE `online_order_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `online_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `online_orders`
--

DROP TABLE IF EXISTS `online_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `online_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `delivery_address` text NOT NULL,
  `order_date` datetime(6) NOT NULL,
  `order_number` varchar(30) NOT NULL,
  `payment_status` varchar(20) NOT NULL,
  `payment_transaction_id` varchar(100) DEFAULT NULL,
  `status` enum('CANCELLED','CONFIRMED','DELIVERED','DISPATCHED','PENDING_VERIFICATION','READY_FOR_PICKUP') NOT NULL,
  `total_amount` double NOT NULL,
  `branch_id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `prescription_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK8oefsau6ljicmeojjg6gx578e` (`order_number`),
  UNIQUE KEY `UK223nwwxquy4stlc3b87ffpfx7` (`prescription_id`),
  KEY `FKb5gh1u6iio9rqhculrek18xrf` (`branch_id`),
  KEY `FKfstkt07etojf70xviub5gdhex` (`customer_id`),
  CONSTRAINT `FKb5gh1u6iio9rqhculrek18xrf` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `FKfstkt07etojf70xviub5gdhex` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FKkis34h4456tr9pudqtloinhh1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `online_orders`
--

LOCK TABLES `online_orders` WRITE;
/*!40000 ALTER TABLE `online_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `online_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL,
  `payment_date` datetime(6) NOT NULL,
  `payment_method` enum('BANK_TRANSFER','BKASH','CARD','CASH','NAGAD','ROCKET') NOT NULL,
  `payment_status` enum('CANCELLED','OVERDUE','PAID','PARTIAL','PENDING') DEFAULT NULL,
  `transaction_reference` varchar(100) DEFAULT NULL,
  `invoice_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK92vx5yfmtmjeunk3xqvxshfi2` (`invoice_id`),
  CONSTRAINT `FK92vx5yfmtmjeunk3xqvxshfi2` FOREIGN KEY (`invoice_id`) REFERENCES `sales_invoices` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `policestations`
--

DROP TABLE IF EXISTS `policestations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `policestations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `policestations`
--

LOCK TABLES `policestations` WRITE;
/*!40000 ALTER TABLE `policestations` DISABLE KEYS */;
INSERT INTO `policestations` VALUES (1,'khilgaon');
/*!40000 ALTER TABLE `policestations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `doctor_name` varchar(150) DEFAULT NULL,
  `file_type` varchar(100) DEFAULT NULL,
  `hospital_or_clinic` varchar(150) DEFAULT NULL,
  `prescription_date` date DEFAULT NULL,
  `remarks` text,
  `scanned_copy` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKngc597c25d8ebdgnmrqjm79fd` (`customer_id`),
  CONSTRAINT `FKngc597c25d8ebdgnmrqjm79fd` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_items`
--

DROP TABLE IF EXISTS `purchase_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `ordered_quantity` int NOT NULL,
  `received_quantity` int NOT NULL,
  `unit_price` decimal(12,2) DEFAULT NULL,
  `medicine_id` bigint NOT NULL,
  `purchase_order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb8eixqn0uouxtyixl57mmuq3a` (`medicine_id`),
  KEY `FKo3yj8ocbw2kav38548t22hgh8` (`purchase_order_id`),
  CONSTRAINT `FKb8eixqn0uouxtyixl57mmuq3a` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`),
  CONSTRAINT `FKo3yj8ocbw2kav38548t22hgh8` FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_items`
--

LOCK TABLES `purchase_order_items` WRITE;
/*!40000 ALTER TABLE `purchase_order_items` DISABLE KEYS */;
INSERT INTO `purchase_order_items` VALUES (1,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,500,500,12.50,1,1),(2,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,300,300,45.00,2,1),(3,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,1000,1000,8.00,3,2),(4,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,250,250,120.00,4,2),(5,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,400,400,22.00,5,3),(6,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,600,600,15.00,6,4),(7,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,150,150,85.00,7,4),(8,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,800,0,18.50,8,5),(9,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,300,0,65.00,9,5),(10,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,500,500,30.00,10,6),(11,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,1000,0,5.50,11,7),(12,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,200,0,150.00,12,7),(13,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,450,0,40.00,13,8),(14,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,700,0,11.00,14,9),(15,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,350,0,95.00,15,10),(16,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,500,0,25.00,16,10),(17,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,600,0,14.00,17,11),(18,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,200,0,210.00,18,12),(19,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,800,800,9.50,19,13),(20,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,100,100,350.00,20,13),(21,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,400,0,12.50,1,14),(22,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,500,0,8.00,3,15),(23,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,300,0,22.00,5,16),(24,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,250,0,85.00,7,17),(25,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,400,0,65.00,9,18),(26,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,1200,0,5.50,11,19),(27,'2026-07-21 12:54:56.000000',NULL,_binary '','2026-07-21 12:54:56.000000',NULL,300,0,40.00,13,20);
/*!40000 ALTER TABLE `purchase_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `expected_delivery_date` date DEFAULT NULL,
  `order_date` date NOT NULL,
  `po_number` varchar(30) NOT NULL,
  `status` enum('APPROVED','CANCELLED','CLOSED','PENDING','RECEIVED','REJECTED') NOT NULL,
  `branch_id` bigint NOT NULL,
  `created_by_user` bigint DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpbiykvcpyg0jslne4gviyeuc2` (`po_number`),
  KEY `FKcro9u222fvejvt89euowf35pq` (`branch_id`),
  KEY `FKph33vm08de8wnma1inrq17u1g` (`created_by_user`),
  KEY `FKrpdasmb8y8xs5tiy4369xpinq` (`supplier_id`),
  CONSTRAINT `FKcro9u222fvejvt89euowf35pq` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `FKph33vm08de8wnma1inrq17u1g` FOREIGN KEY (`created_by_user`) REFERENCES `users` (`id`),
  CONSTRAINT `FKrpdasmb8y8xs5tiy4369xpinq` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
INSERT INTO `purchase_orders` VALUES (1,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-05','2026-06-01','PO-2026-0001','RECEIVED',1,NULL,1),(2,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-07','2026-06-03','PO-2026-0002','RECEIVED',1,NULL,2),(3,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-10','2026-06-05','PO-2026-0003','RECEIVED',2,NULL,3),(4,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-15','2026-06-10','PO-2026-0004','RECEIVED',1,NULL,4),(5,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-18','2026-06-12','PO-2026-0005','APPROVED',3,NULL,5),(6,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-20','2026-06-15','PO-2026-0006','RECEIVED',2,NULL,6),(7,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-25','2026-06-20','PO-2026-0007','APPROVED',1,NULL,7),(8,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-28','2026-06-22','PO-2026-0008','APPROVED',3,NULL,8),(9,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-06-30','2026-06-25','PO-2026-0009','APPROVED',2,NULL,9),(10,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 14:16:31.989791',NULL,'2026-07-06','2026-07-01','PO-2026-0010','RECEIVED',1,NULL,10),(11,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-08','2026-07-03','PO-2026-0011','PENDING',3,NULL,11),(12,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-10','2026-07-05','PO-2026-0012','APPROVED',1,NULL,12),(13,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-13','2026-07-08','PO-2026-0013','CLOSED',2,NULL,13),(14,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 14:14:41.174560',NULL,'2026-07-15','2026-07-10','PO-2026-0014','APPROVED',1,NULL,14),(15,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-17','2026-07-12','PO-2026-0015','CANCELLED',3,NULL,15),(16,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-19','2026-07-14','PO-2026-0016','REJECTED',2,NULL,16),(17,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-20','2026-07-15','PO-2026-0017','APPROVED',1,NULL,17),(18,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-22','2026-07-17','PO-2026-0018','PENDING',3,NULL,18),(19,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-24','2026-07-19','PO-2026-0019','PENDING',2,NULL,19),(20,'2026-07-21 12:51:45.000000',NULL,_binary '','2026-07-21 12:51:45.000000',NULL,'2026-07-25','2026-07-20','PO-2026-0020','PENDING',1,NULL,20);
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_return_items`
--

DROP TABLE IF EXISTS `purchase_return_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_return_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `credit_amount` decimal(12,2) DEFAULT NULL,
  `quantity` int NOT NULL,
  `reason` enum('CUSTOMER_DISSATISFACTION','DAMAGED','EXPIRED','OTHER','OVERSTOCK','QUALITY_ISSUE','WRONG_ITEM_DELIVERED') DEFAULT NULL,
  `batch_id` bigint NOT NULL,
  `purchase_return_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1eppir0bfi1hcwlsad6i0do6g` (`batch_id`),
  KEY `FK39pddnn1weajlxm2bssnpwsbh` (`purchase_return_id`),
  CONSTRAINT `FK1eppir0bfi1hcwlsad6i0do6g` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`),
  CONSTRAINT `FK39pddnn1weajlxm2bssnpwsbh` FOREIGN KEY (`purchase_return_id`) REFERENCES `purchase_returns` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_return_items`
--

LOCK TABLES `purchase_return_items` WRITE;
/*!40000 ALTER TABLE `purchase_return_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_return_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_returns`
--

DROP TABLE IF EXISTS `purchase_returns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_returns` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `return_date` date NOT NULL,
  `return_number` varchar(30) NOT NULL,
  `branch_id` bigint NOT NULL,
  `supplier_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKdgl4xfqh6swnjwtaew79wrxh0` (`return_number`),
  KEY `FKexd8kyxlr918q7fc40xc6y353` (`branch_id`),
  KEY `FK3m60bx40fnvbybfh3diblu89h` (`supplier_id`),
  CONSTRAINT `FK3m60bx40fnvbybfh3diblu89h` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `FKexd8kyxlr918q7fc40xc6y353` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_returns`
--

LOCK TABLES `purchase_returns` WRITE;
/*!40000 ALTER TABLE `purchase_returns` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_returns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requisition_items`
--

DROP TABLE IF EXISTS `requisition_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requisition_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `approved_quantity` int DEFAULT NULL,
  `fulfilled_quantity` int NOT NULL,
  `requested_quantity` int NOT NULL,
  `medicine_id` bigint NOT NULL,
  `requisition_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf8rofabq6pn4w7n6yqxyjs1np` (`medicine_id`),
  KEY `FKqqpkdlg5r4elr4pb5c630tboh` (`requisition_id`),
  CONSTRAINT `FKf8rofabq6pn4w7n6yqxyjs1np` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`),
  CONSTRAINT `FKqqpkdlg5r4elr4pb5c630tboh` FOREIGN KEY (`requisition_id`) REFERENCES `requisitions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requisition_items`
--

LOCK TABLES `requisition_items` WRITE;
/*!40000 ALTER TABLE `requisition_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `requisition_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requisitions`
--

DROP TABLE IF EXISTS `requisitions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requisitions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `priority` enum('HIGH','LOW','MEDIUM','URGENT') DEFAULT NULL,
  `remarks` text,
  `requisition_date` date NOT NULL,
  `requisition_number` varchar(30) NOT NULL,
  `status` enum('APPROVED','CANCELLED','DRAFT','FULFILLED','PARTIALLY_APPROVED','PARTIALLY_FULFILLED','PROCESSING','REJECTED','SUBMITTED','UNDER_REVIEW') NOT NULL,
  `approved_by` bigint DEFAULT NULL,
  `branch_id` bigint NOT NULL,
  `requested_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhucy81lfnrxf8ouvlsrauvb5u` (`requisition_number`),
  KEY `FKb0j5oipqc5lll1kgrff0cfqeg` (`approved_by`),
  KEY `FKl8lhoojjk7q6x32a14yjrilue` (`branch_id`),
  KEY `FKd9edky901hjyvak0gdcquiigb` (`requested_by`),
  CONSTRAINT `FKb0j5oipqc5lll1kgrff0cfqeg` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKd9edky901hjyvak0gdcquiigb` FOREIGN KEY (`requested_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKl8lhoojjk7q6x32a14yjrilue` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requisitions`
--

LOCK TABLES `requisitions` WRITE;
/*!40000 ALTER TABLE `requisitions` DISABLE KEYS */;
/*!40000 ALTER TABLE `requisitions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_invoice_items`
--

DROP TABLE IF EXISTS `sales_invoice_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_invoice_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `discount_type` enum('FIXED_AMOUNT','PERCENTAGE') DEFAULT NULL,
  `discount_value` decimal(12,2) DEFAULT NULL,
  `line_total` decimal(12,2) DEFAULT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(12,2) DEFAULT NULL,
  `batch_id` bigint NOT NULL,
  `invoice_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK90lc6avu6979bg5ud8d19n63h` (`batch_id`),
  KEY `FK47gumte04iloouebl3f1w3erx` (`invoice_id`),
  CONSTRAINT `FK47gumte04iloouebl3f1w3erx` FOREIGN KEY (`invoice_id`) REFERENCES `sales_invoices` (`id`),
  CONSTRAINT `FK90lc6avu6979bg5ud8d19n63h` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_invoice_items`
--

LOCK TABLES `sales_invoice_items` WRITE;
/*!40000 ALTER TABLE `sales_invoice_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_invoice_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_invoices`
--

DROP TABLE IF EXISTS `sales_invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `discount_amount` decimal(12,2) DEFAULT NULL,
  `due_amount` decimal(12,2) DEFAULT NULL,
  `invoice_date` datetime(6) NOT NULL,
  `invoice_number` varchar(30) NOT NULL,
  `paid_amount` decimal(12,2) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','DRAFT','PAID','PARTIALLY_PAID','REFUNDED') NOT NULL,
  `sub_total` decimal(12,2) DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT NULL,
  `vat_amount` decimal(12,2) DEFAULT NULL,
  `branch_id` bigint NOT NULL,
  `customer_id` bigint DEFAULT NULL,
  `online_order_id` bigint DEFAULT NULL,
  `prescription_id` bigint DEFAULT NULL,
  `sold_by` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgn9ujwddfu20nghov7s7qubal` (`invoice_number`),
  UNIQUE KEY `UKbyfdi3odqxikw23ppevsnxut3` (`online_order_id`),
  KEY `FK99tovk7atflk1sqe0uf7x9p8v` (`branch_id`),
  KEY `FK6boqj01yqbp2q3q30w7lh1ijf` (`customer_id`),
  KEY `FK40d0rt7emdtd54greeean9ave` (`prescription_id`),
  KEY `FKblqvnklh6emxb7a9yajp89fl7` (`sold_by`),
  CONSTRAINT `FK40d0rt7emdtd54greeean9ave` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`id`),
  CONSTRAINT `FK6boqj01yqbp2q3q30w7lh1ijf` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FK99tovk7atflk1sqe0uf7x9p8v` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `FKblqvnklh6emxb7a9yajp89fl7` FOREIGN KEY (`sold_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj4abaoacw987n6qf0udptoikc` FOREIGN KEY (`online_order_id`) REFERENCES `online_orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_invoices`
--

LOCK TABLES `sales_invoices` WRITE;
/*!40000 ALTER TABLE `sales_invoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_return_items`
--

DROP TABLE IF EXISTS `sales_return_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_return_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `quantity` int NOT NULL,
  `reason` enum('CUSTOMER_DISSATISFACTION','DAMAGED','EXPIRED','OTHER','OVERSTOCK','QUALITY_ISSUE','WRONG_ITEM_DELIVERED') DEFAULT NULL,
  `refund_amount` decimal(12,2) DEFAULT NULL,
  `batch_id` bigint NOT NULL,
  `sales_return_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo693x1gmk137m62d500cttbi` (`batch_id`),
  KEY `FK6bndgkqb5vxhudpbwgw74y6i8` (`sales_return_id`),
  CONSTRAINT `FK6bndgkqb5vxhudpbwgw74y6i8` FOREIGN KEY (`sales_return_id`) REFERENCES `sales_returns` (`id`),
  CONSTRAINT `FKo693x1gmk137m62d500cttbi` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_return_items`
--

LOCK TABLES `sales_return_items` WRITE;
/*!40000 ALTER TABLE `sales_return_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_return_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_returns`
--

DROP TABLE IF EXISTS `sales_returns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_returns` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `return_date` date NOT NULL,
  `return_number` varchar(30) NOT NULL,
  `invoice_id` bigint NOT NULL,
  `processed_by` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjel0imk2fx0xg10s8hn38l712` (`return_number`),
  KEY `FK2049kdwikg7eurihgq3ltmkij` (`invoice_id`),
  KEY `FKpdt2dr3lknxih955txkqd6eij` (`processed_by`),
  CONSTRAINT `FK2049kdwikg7eurihgq3ltmkij` FOREIGN KEY (`invoice_id`) REFERENCES `sales_invoices` (`id`),
  CONSTRAINT `FKpdt2dr3lknxih955txkqd6eij` FOREIGN KEY (`processed_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_returns`
--

LOCK TABLES `sales_returns` WRITE;
/*!40000 ALTER TABLE `sales_returns` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_returns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_adjustment_items`
--

DROP TABLE IF EXISTS `stock_adjustment_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_adjustment_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `quantity_after` int DEFAULT NULL,
  `quantity_before` int DEFAULT NULL,
  `reason` enum('DAMAGED','EXPIRED','OTHER','PHYSICAL_COUNT_MISMATCH','SYSTEM_ERROR_CORRECTION','THEFT_OR_LOSS') DEFAULT NULL,
  `remarks` text,
  `batch_id` bigint NOT NULL,
  `stock_adjustment_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8k6fcfx8illh9b8o8c2ir4t8b` (`batch_id`),
  KEY `FKrfuhgt0uc7r2cm5fb3deqi4yn` (`stock_adjustment_id`),
  CONSTRAINT `FK8k6fcfx8illh9b8o8c2ir4t8b` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`),
  CONSTRAINT `FKrfuhgt0uc7r2cm5fb3deqi4yn` FOREIGN KEY (`stock_adjustment_id`) REFERENCES `stock_adjustments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_adjustment_items`
--

LOCK TABLES `stock_adjustment_items` WRITE;
/*!40000 ALTER TABLE `stock_adjustment_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_adjustment_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_adjustments`
--

DROP TABLE IF EXISTS `stock_adjustments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_adjustments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `adjustment_date` date NOT NULL,
  `adjustment_number` varchar(30) NOT NULL,
  `approved_by` bigint DEFAULT NULL,
  `branch_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKnmisxi6vihfcie4tbnvvnaoya` (`adjustment_number`),
  KEY `FKcyhsu2w9g9mj8ek3h4mqox3nb` (`approved_by`),
  KEY `FKsdsn36525iq0jdc0quh16g600` (`branch_id`),
  CONSTRAINT `FKcyhsu2w9g9mj8ek3h4mqox3nb` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsdsn36525iq0jdc0quh16g600` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_adjustments`
--

LOCK TABLES `stock_adjustments` WRITE;
/*!40000 ALTER TABLE `stock_adjustments` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_adjustments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_movements`
--

DROP TABLE IF EXISTS `stock_movements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_movements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `movement_date` datetime(6) NOT NULL,
  `movement_type` enum('ADJUSTMENT_DECREASE','ADJUSTMENT_INCREASE','DAMAGED_WRITE_OFF','EXPIRED_WRITE_OFF','PURCHASE_RECEIVED','PURCHASE_RETURN','SALE','SALE_RETURN','TRANSFER_IN','TRANSFER_OUT') NOT NULL,
  `quantity` int NOT NULL,
  `reference_id` bigint DEFAULT NULL,
  `reference_type` varchar(50) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `batch_id` bigint NOT NULL,
  `branch_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbb54gmh6li4m6hcoy9ai56gvu` (`batch_id`),
  KEY `FKknptp2x5ng8ghcyikkxn5v7rj` (`branch_id`),
  CONSTRAINT `FKbb54gmh6li4m6hcoy9ai56gvu` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`),
  CONSTRAINT `FKknptp2x5ng8ghcyikkxn5v7rj` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_movements`
--

LOCK TABLES `stock_movements` WRITE;
/*!40000 ALTER TABLE `stock_movements` DISABLE KEYS */;
INSERT INTO `stock_movements` VALUES (1,'2026-07-20 15:33:53.482114',NULL,_binary '','2026-07-20 15:33:53.482114',NULL,'2026-07-20 15:33:53.481143','PURCHASE_RECEIVED',500,1,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-216956',1,1),(2,'2026-07-21 13:10:17.745929',NULL,_binary '','2026-07-21 13:10:17.745929',NULL,'2026-07-21 13:10:17.745929','PURCHASE_RECEIVED',1000,7,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0007',1,1),(3,'2026-07-21 13:10:17.756391',NULL,_binary '','2026-07-21 13:10:17.756391',NULL,'2026-07-21 13:10:17.756391','PURCHASE_RECEIVED',200,7,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0007',2,1),(4,'2026-07-21 13:19:19.082494',NULL,_binary '','2026-07-21 13:19:19.082494',NULL,'2026-07-21 13:19:19.082494','PURCHASE_RECEIVED',500,1,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0001',1,1),(5,'2026-07-21 13:19:19.093979',NULL,_binary '','2026-07-21 13:19:19.093979',NULL,'2026-07-21 13:19:19.092984','PURCHASE_RECEIVED',300,1,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0001',2,1),(6,'2026-07-21 13:19:30.108638',NULL,_binary '','2026-07-21 13:19:30.108638',NULL,'2026-07-21 13:19:30.108638','PURCHASE_RECEIVED',1000,2,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0002',3,1),(7,'2026-07-21 13:19:30.117857',NULL,_binary '','2026-07-21 13:19:30.117857',NULL,'2026-07-21 13:19:30.117857','PURCHASE_RECEIVED',250,2,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0002',4,1),(8,'2026-07-21 13:19:37.504477',NULL,_binary '','2026-07-21 13:19:37.504477',NULL,'2026-07-21 13:19:37.504477','PURCHASE_RECEIVED',400,3,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0003',5,2),(9,'2026-07-21 13:19:43.969359',NULL,_binary '','2026-07-21 13:19:43.969359',NULL,'2026-07-21 13:19:43.969359','PURCHASE_RECEIVED',600,4,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0004',6,1),(10,'2026-07-21 13:19:43.973349',NULL,_binary '','2026-07-21 13:19:43.973349',NULL,'2026-07-21 13:19:43.973349','PURCHASE_RECEIVED',150,4,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0004',7,1),(11,'2026-07-21 13:19:53.617690',NULL,_binary '','2026-07-21 13:19:53.617690',NULL,'2026-07-21 13:19:53.617690','PURCHASE_RECEIVED',800,5,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0005',8,3),(12,'2026-07-21 13:19:53.622693',NULL,_binary '','2026-07-21 13:19:53.622693',NULL,'2026-07-21 13:19:53.622693','PURCHASE_RECEIVED',300,5,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0005',9,3),(13,'2026-07-21 13:20:06.664498',NULL,_binary '','2026-07-21 13:20:06.664498',NULL,'2026-07-21 13:20:06.664498','PURCHASE_RECEIVED',500,6,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0006',10,2),(14,'2026-07-21 13:20:18.298998',NULL,_binary '','2026-07-21 13:20:18.298998',NULL,'2026-07-21 13:20:18.298008','PURCHASE_RECEIVED',1000,7,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0007',11,1),(15,'2026-07-21 13:20:18.306791',NULL,_binary '','2026-07-21 13:20:18.306791',NULL,'2026-07-21 13:20:18.306791','PURCHASE_RECEIVED',200,7,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0007',12,1),(16,'2026-07-21 13:20:53.300477',NULL,_binary '','2026-07-21 13:20:53.300477',NULL,'2026-07-21 13:20:53.299490','PURCHASE_RECEIVED',450,8,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0008',13,3),(17,'2026-07-21 13:21:08.022641',NULL,_binary '','2026-07-21 13:21:08.022641',NULL,'2026-07-21 13:21:08.022641','PURCHASE_RECEIVED',700,9,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0009',14,2),(18,'2026-07-21 13:21:15.779130',NULL,_binary '','2026-07-21 13:21:15.779130',NULL,'2026-07-21 13:21:15.779130','PURCHASE_RECEIVED',350,10,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0010',15,1),(19,'2026-07-21 13:21:15.783108',NULL,_binary '','2026-07-21 13:21:15.783108',NULL,'2026-07-21 13:21:15.783108','PURCHASE_RECEIVED',500,10,'GOODS_RECEIVED_NOTE','Inventory stocked inward via GRN: GRN-2026-0010',16,1),(20,'2026-07-21 14:16:31.969291',NULL,_binary '','2026-07-21 14:16:31.969291',NULL,'2026-07-21 14:16:31.968295','PURCHASE_RECEIVED',350,10,'PURCHASE_ORDER','Stock inward generated via Purchase Order fulfillment matching invoice tracker: PO-2026-0010',1,1),(21,'2026-07-21 14:16:31.976720',NULL,_binary '','2026-07-21 14:16:31.976720',NULL,'2026-07-21 14:16:31.976720','PURCHASE_RECEIVED',500,10,'PURCHASE_ORDER','Stock inward generated via Purchase Order fulfillment matching invoice tracker: PO-2026-0010',1,1);
/*!40000 ALTER TABLE `stock_movements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_transfer_items`
--

DROP TABLE IF EXISTS `stock_transfer_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_transfer_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `received_quantity` int DEFAULT NULL,
  `sent_quantity` int NOT NULL,
  `batch_id` bigint NOT NULL,
  `stock_transfer_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlnvtg8f7vl17ru98rp1lkwntv` (`batch_id`),
  KEY `FK94w6ybx0p94igbd4dvolqlurl` (`stock_transfer_id`),
  CONSTRAINT `FK94w6ybx0p94igbd4dvolqlurl` FOREIGN KEY (`stock_transfer_id`) REFERENCES `stock_transfers` (`id`),
  CONSTRAINT `FKlnvtg8f7vl17ru98rp1lkwntv` FOREIGN KEY (`batch_id`) REFERENCES `medicine_batches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_transfer_items`
--

LOCK TABLES `stock_transfer_items` WRITE;
/*!40000 ALTER TABLE `stock_transfer_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_transfer_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_transfers`
--

DROP TABLE IF EXISTS `stock_transfers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_transfers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `status` enum('CANCELLED','DELIVERED','DISPATCHED','IN_TRANSIT','PACKED','PARTIALLY_DELIVERED','PENDING','RECEIVED') NOT NULL,
  `transfer_date` date NOT NULL,
  `transfer_number` varchar(30) NOT NULL,
  `dispatched_by` bigint DEFAULT NULL,
  `from_branch_id` bigint NOT NULL,
  `received_by` bigint DEFAULT NULL,
  `requisition_id` bigint DEFAULT NULL,
  `to_branch_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKick25fueer7ic8lpb758bj6b7` (`transfer_number`),
  KEY `FKjy6e1gaoqumdpv1eod9lcdw9x` (`dispatched_by`),
  KEY `FKfl7cgq3gi53g313v51dky6pd5` (`from_branch_id`),
  KEY `FK36sb6imenkwgb42v2vj0mj80h` (`received_by`),
  KEY `FKsjvk5jny43y2ct02ws1s5n70p` (`requisition_id`),
  KEY `FKsqcl96ejd42qe81pqw69wj1vc` (`to_branch_id`),
  CONSTRAINT `FK36sb6imenkwgb42v2vj0mj80h` FOREIGN KEY (`received_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKfl7cgq3gi53g313v51dky6pd5` FOREIGN KEY (`from_branch_id`) REFERENCES `branches` (`id`),
  CONSTRAINT `FKjy6e1gaoqumdpv1eod9lcdw9x` FOREIGN KEY (`dispatched_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKsjvk5jny43y2ct02ws1s5n70p` FOREIGN KEY (`requisition_id`) REFERENCES `requisitions` (`id`),
  CONSTRAINT `FKsqcl96ejd42qe81pqw69wj1vc` FOREIGN KEY (`to_branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_transfers`
--

LOCK TABLES `stock_transfers` WRITE;
/*!40000 ALTER TABLE `stock_transfers` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_transfers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `supplier_code` varchar(20) NOT NULL,
  `tax_id` varchar(100) DEFAULT NULL,
  `trade_license_no` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqlclyj0vn5vwtb86objyhmlkx` (`supplier_code`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Square Centre, 48 Mohakhali C/A','Suite 4B','Dhaka','Bangladesh','1212','Dhaka Division','Tanvir Ahmed','supply@squarepharma.com','Square Pharmaceuticals Ltd.','+8801711000001','SUP-001','TIN-98765432101','TRAD/DNCC/012345/2021'),(2,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','40 Shahid Tajuddin Ahmed Sarani','Tejgaon I/A','Dhaka','Bangladesh','1208','Dhaka Division','Rafiqul Islam','sales@inceptapharma.com','Incepta Pharmaceuticals Ltd.','+8801711000002','SUP-002','TIN-98765432102','TRAD/DNCC/012346/2021'),(3,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','19 Dhanmondi R/A','Road No. 7','Dhaka','Bangladesh','1205','Dhaka Division','Kamrul Hasan','info@beximcopharma.com','Beximco Pharmaceuticals Ltd.','+8801711000003','SUP-003','TIN-98765432103','TRAD/DSCC/012347/2021'),(4,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Nasir Uddin Tower','104 Bir Uttam C.R. Datta Road','Dhaka','Bangladesh','1205','Dhaka Division','Mahmudur Rahman','distribution@hpl.com.bd','Healthcare Pharmaceuticals Ltd.','+8801711000004','SUP-004','TIN-98765432104','TRAD/DSCC/012348/2021'),(5,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Plot 1, Milk Vita Road','Section 7, Mirpur','Dhaka','Bangladesh','1216','Dhaka Division','Sajid Hossain','supplychain@renata-ltd.com','Renata Limited','+8801711000005','SUP-005','TIN-98765432105','TRAD/DNCC/012349/2021'),(6,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','ACI Centre','245 Tejgaon Industrial Area','Dhaka','Bangladesh','1208','Dhaka Division','Ahsan Habib','orders@aci-bd.com','ACI Healthcare Limited','+8801711000006','SUP-006','TIN-98765432106','TRAD/DNCC/012350/2021'),(7,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','1/4 Kalyanpur','Mirpur Road','Dhaka','Bangladesh','1207','Dhaka Division','Shahadat Hossain','marketing@acmeglobal.com','Acme Laboratories Ltd.','+8801711000007','SUP-007','TIN-98765432107','TRAD/DNCC/012351/2021'),(8,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','7-8 Principal Road','Baridhara','Dhaka','Bangladesh','1212','Dhaka Division','Zillur Rahman','contact@aristopharma.com','Aristopharma Ltd.','+8801711000008','SUP-008','TIN-98765432108','TRAD/DNCC/012352/2021'),(9,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Gulshan Tower, Plot 31','North Avenue, Gulshan 2','Dhaka','Bangladesh','1212','Dhaka Division','Anisur Rahman','info@skf.com.bd','Eskayef Pharmaceuticals Ltd.','+8801711000009','SUP-009','TIN-98765432109','TRAD/DNCC/012353/2021'),(10,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','30 New Eskaton Road','Apt 5A','Dhaka','Bangladesh','1000','Dhaka Division','Mizanur Rahman','sales@opsonin-pharma.com','Opsonin Pharma Ltd.','+8801711000010','SUP-010','TIN-98765432110','TRAD/DSCC/012354/2021'),(11,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','17/1-1, Sheltech Tower','Kazi Nazrul Islam Avenue','Dhaka','Bangladesh','1215','Dhaka Division','Naimul Islam','supply@popularpharma.com','Popular Pharmaceuticals Ltd.','+8801711000011','SUP-011','TIN-98765432111','TRAD/DSCC/012355/2021'),(12,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','22/1 Rasheed Tower','Bir Uttam A.K. Khandakar Road','Dhaka','Bangladesh','1212','Dhaka Division','Sabbir Ahmed','info@radiantpharma.com','Radiant Pharmaceuticals Ltd.','+8801711000012','SUP-012','TIN-98765432112','TRAD/DNCC/012356/2021'),(13,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Khwaja Enayetpuri Tower','17 Green Road','Dhaka','Bangladesh','1205','Dhaka Division','Jahangir Alam','sales@drug-international.com','Drug International Ltd.','+8801711000013','SUP-013','TIN-98765432113','TRAD/DSCC/012357/2021'),(14,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Beacon Building','Plot 9, Road 14, Block B','Dhaka','Bangladesh','1213','Dhaka Division','Mustafizur Rahman','beacon@beaconpharma.com.bd','Beacon Pharmaceuticals Ltd.','+8801711000014','SUP-014','TIN-98765432114','TRAD/DNCC/012358/2021'),(15,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','60 Dilkusha C/A','Motijheel','Dhaka','Bangladesh','1000','Dhaka Division','Faisal Mahmud','contact@sanofi.com.bd','Sanofi Bangladesh Ltd.','+8801711000015','SUP-015','TIN-98765432115','TRAD/DSCC/012359/2021'),(16,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Fouzderhat I/A','P.O. Box 88','Chittagong','Bangladesh','4201','Chittagong Division','Rashedul Karim','gsk.bd@gsk.com','GlaxoSmithKline Bangladesh','+8801711000016','SUP-016','TIN-98765432116','TRAD/CCC/012360/2021'),(17,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Orion House','153-154 Tejgaon I/A','Dhaka','Bangladesh','1208','Dhaka Division','Kazi Tareq','info@orionpharma.net','Orion Pharma Ltd.','+8801711000017','SUP-017','TIN-98765432117','TRAD/DNCC/012361/2021'),(18,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','Tanin Center','100 Asad Avenue, Mohammadpur','Dhaka','Bangladesh','1207','Dhaka Division','Shamsul Huda','sales@ibnsinapharma.com','Ibn Sina Pharmaceutical Ind.','+8801711000018','SUP-018','TIN-98765432118','TRAD/DNCC/012362/2021'),(19,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','House 39, Road 27','Block A, Banani','Dhaka','Bangladesh','1213','Dhaka Division','Moklesur Rahman','ziska@ziskapharma.com','Ziska Pharmaceuticals Ltd.','+8801711000019','SUP-019','TIN-98765432119','TRAD/DNCC/012363/2021'),(20,'2026-07-21 12:28:09.000000','SYSTEM',_binary '','2026-07-21 12:28:09.000000','SYSTEM','103 Agrabad C/A','Chittagong Main Office','Chittagong','Bangladesh','4100','Chittagong Division','Ariful Islam','info@nuvistapharma.com','Nuvista Pharma Ltd.','+8801711000020','SUP-020','TIN-98765432120','TRAD/CCC/012364/2021');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('ACCOUNTANT','BRANCH_MANAGER','CENTRAL_ADMIN','CUSTOMER','PHARMACIST','SALESMAN','STOCK_KEEPER','SUPER_ADMIN') NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `branch_id` bigint DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FK9o70sp9ku40077y38fk4wieyk` (`branch_id`),
  CONSTRAINT `FK9o70sp9ku40077y38fk4wieyk` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2026-07-06 17:57:43.009611',NULL,_binary '','2026-07-06 18:03:40.663604',NULL,'gohinshagor123@gmail.com',_binary '','Arif Hossain','$2a$10$sJaJb6fzuyeQVvUF6TLOG.szFSRevdswG42zBl52/QTJ0zRln4v..',NULL,'CUSTOMER',NULL,NULL,NULL),(18,'2026-07-20 15:30:47.403758',NULL,_binary '','2026-07-20 15:32:53.759627',NULL,'mishkatulkazi@gmail.com',_binary '','mishkatul','$2a$10$EVBTzTKCd9LdtvSlQF7PFue9JyTsW.lz60ZTcJBeeHvoeIXemVK4y','55656','BRANCH_MANAGER','dfdf',1,NULL),(19,'2026-07-21 11:26:16.840061',NULL,_binary '','2026-07-21 11:27:33.740265',NULL,'mishkatulbd@gmail.com',_binary '','ghghg','$2a$10$5SsWYGKxGZuq18lhFMjf9ugzzBsjuCSqMRQG.bbYBbyx/h1snBltG','656565','CUSTOMER','fgfgfgf',NULL,'ghghg_2ef6ee3b-6599-46b5-8115-33a76cc28b77.png');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-21 14:34:07
