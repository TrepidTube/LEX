CREATE TABLE `usuarios` (
  `id` integer PRIMARY KEY,
  `username` varchar(20),
  `nombre` varchar(50),
  `apellido` varchar(50),
  `numero_tel` integer(10),
  `id_rol` integer,
  `fecha_creacion` timestamp
);

CREATE TABLE `casos` (
  `caso_type` varchar(255) PRIMARY KEY,
  `caso_creacion` datetime,
  `caso_mod` datetime,
  `doc_name` varchar(200),
  `doc_type` varchar(10)
);

CREATE TABLE `roles` (
  `id_rol` integer PRIMARY KEY,
  `rol_name` varchar(20)
);

CREATE TABLE `agenda` (
  `id` integer PRIMARY KEY,
  `caso_type` varchar(255),
  `id_rol` integer,
  `fecha_creacion` datetime,
  `fecha_mod` datetime
);

ALTER TABLE `roles` ADD FOREIGN KEY (`id_rol`) REFERENCES `usuarios` (`id_rol`);

ALTER TABLE `usuarios` ADD FOREIGN KEY (`id`) REFERENCES `agenda` (`id`);

ALTER TABLE `roles` ADD FOREIGN KEY (`id_rol`) REFERENCES `agenda` (`id_rol`);

ALTER TABLE `casos` ADD FOREIGN KEY (`caso_type`) REFERENCES `agenda` (`caso_type`);
