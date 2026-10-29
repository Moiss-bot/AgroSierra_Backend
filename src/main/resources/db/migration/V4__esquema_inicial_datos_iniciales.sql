
-- V4: datos iniciales de lotes agrícolas

INSERT INTO lotes (
    codigo,
    nombre,
    hectareas,
    cultivo,
    valle,
    campania_cerrada,
    dosis_maxima_por_hectarea
)
VALUES (
           'LOT-001',
           'Lote Norte',
           12.5,
           'Papa',
           'Mantaro',
           FALSE,
           4.0
       );

INSERT INTO lotes (
    codigo,
    nombre,
    hectareas,
    cultivo,
    valle,
    campania_cerrada,
    dosis_maxima_por_hectarea
)
VALUES (
           'LOT-002',
           'Lote Sur',
           8.0,
           'Maiz',
           'Mantaro',
           FALSE,
           3.5
       );

INSERT INTO lotes (
    codigo,
    nombre,
    hectareas,
    cultivo,
    valle,
    campania_cerrada,
    dosis_maxima_por_hectarea
)
VALUES (
           'LOT-003',
           'Lote Central',
           15.0,
           'Quinua',
           'Colca',
           FALSE,
           5.0
       );
