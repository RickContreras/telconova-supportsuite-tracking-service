# Guía para contribuir al proyecto

## Proceso de desarrollo

1. **Ramas**: Utilizamos GitFlow como flujo de trabajo
   - `main`: Producción
   - `develop`: Integración
   - `feature/*`: Para nuevas características
   - `bugfix/*`: Para corrección de errores
   - `hotfix/*`: Para correcciones urgentes en producción

2. **Pull Requests**:
   - Crear PR desde tu rama a `develop`
   - Requiere al menos 1 aprobación
   - Los tests deben pasar

## Estándares de código

### Java
- Seguir las convenciones de Google Java Style Guide
- Máximo 120 caracteres por línea
- Documentar clases y métodos públicos con JavaDoc
- Evitar métodos con más de 50 líneas

### Pruebas
- Cada clase debe tener su correspondiente clase de test
- Nombrar los métodos de test con el formato: `should[ExpectedBehavior]When[StateUnderTest]`

## Commits

- Utilizar commits atómicos (un commit por cambio lógico)
- Seguir Conventional Commits: `type(scope): message`
  - `feat`: Nueva funcionalidad
  - `fix`: Corrección de errores
  - `docs`: Documentación
  - `style`: Cambios de formato
  - `refactor`: Refactorización de código
  - `test`: Añadir/modificar tests
  - `chore`: Tareas de mantenimiento

Ejemplo: `feat(avances): implementar endpoint para registro de comentarios`

## Revisión de código

### Criterios de revisión
- Corrección funcional
- Rendimiento y escalabilidad
- Seguridad
- Legibilidad y mantenibilidad
- Cobertura de pruebas

### Lista de verificación
- [ ] El código cumple con los requisitos funcionales
- [ ] Tiene pruebas unitarias y de integración
- [ ] Sigue las convenciones de estilo
- [ ] No contiene credenciales o información sensible
- [ ] No hay TODOs o código comentado sin justificar
- [ ] La documentación está actualizada

## Herramientas de desarrollo

- **IDE recomendado**: Visual Studio Code

## Flujo de trabajo local

1. Clonar el repositorio
2. Crear una nueva rama desde `develop`
3. Implementar cambios y añadir pruebas
4. Ejecutar pruebas localmente
5. Hacer commits según las convenciones
6. Crear Pull Request