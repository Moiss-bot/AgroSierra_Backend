
-- ============================================================
-- V3: DATOS INICIALES DE SEGURIDAD - AGROSIERRA
-- ============================================================
-- Usuarios:
-- admin   / Admin123
-- jefe    / Jefe123
-- tecnico / Tecnico123
--
-- Contrasenas cifradas con BCrypt (coste 10).
-- ============================================================

-- 1. EMPLEADOS

INSERT INTO empleados (
    dni, nombres, apellidos, cargo,
    email, telefono, estado, fecha_creacion
)
VALUES (
           '70123456', 'Carlos', 'Ramirez Torres',
           'Administrador',
           'carlos.ramirez@agrosierra.pe',
           '987654321', TRUE, SYSTIMESTAMP
       );

INSERT INTO empleados (
    dni, nombres, apellidos, cargo,
    email, telefono, estado, fecha_creacion
)
VALUES (
           '71234567', 'Lucia', 'Fernandez Quispe',
           'Jefe de Campo',
           'lucia.fernandez@agrosierra.pe',
           '987111222', TRUE, SYSTIMESTAMP
       );

INSERT INTO empleados (
    dni, nombres, apellidos, cargo,
    email, telefono, estado, fecha_creacion
)
VALUES (
           '72345678', 'Jorge', 'Mendoza Huaman',
           'Tecnico Agricola',
           'jorge.mendoza@agrosierra.pe',
           '987333444', TRUE, SYSTIMESTAMP
       );

-- 2. USUARIOS CON CONTRASENAS BCrypt

-- admin / Admin123
INSERT INTO usuarios (
    username, password, estado,
    empleado_id, fecha_creacion
)
SELECT
    'admin',
    '$2a$10$lcTrauMzRSNWRHJ.OP.t2uvrzbH6ONyXHwW0QAqFQFnEy4qMT2Lyq',
    TRUE,
    id,
    SYSTIMESTAMP
FROM empleados
WHERE dni = '70123456';

-- jefe / Jefe123
INSERT INTO usuarios (
    username, password, estado,
    empleado_id, fecha_creacion
)
SELECT
    'jefe',
    '$2a$10$RYNwOD6Cz9zrqUZMgsTEyekF0CKITGqDy1l3p4lgs9sw7o8z91w42',
    TRUE,
    id,
    SYSTIMESTAMP
FROM empleados
WHERE dni = '71234567';

-- tecnico / Tecnico123
INSERT INTO usuarios (
    username, password, estado,
    empleado_id, fecha_creacion
)
SELECT
    'tecnico',
    '$2a$10$VOePHKg9DEn6rAjjITkNd.Gn38EpXozYCZVZ2xiJHDmo/SzOv1uem',
    TRUE,
    id,
    SYSTIMESTAMP
FROM empleados
WHERE dni = '72345678';

-- 3. ASIGNACION DE ROLES

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
         CROSS JOIN roles r
WHERE u.username = 'admin'
  AND r.nombre = 'ADMIN';

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
         CROSS JOIN roles r
WHERE u.username = 'jefe'
  AND r.nombre = 'JEFE_CAMPO';

INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u
         CROSS JOIN roles r
WHERE u.username = 'tecnico'
  AND r.nombre = 'TECNICO';
