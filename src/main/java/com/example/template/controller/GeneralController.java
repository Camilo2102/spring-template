package com.example.template.controller;

import com.example.template.models.GeneralModel;
import com.example.template.utils.IdGeneratorUtil;
import com.example.template.utils.RequestUtil;
import com.example.template.constants.MessageConstants;
import com.example.template.constants.RoutesConstants;
import com.example.template.service.GeneralService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.template.utils.IdGeneratorUtil.generateUUID;

/**
 * Controlador general que provee al controllador la funcionalidad basicad el crud, y las rutas establecidas en el crudController, en caso de necesitar personalizar el metodo usar @Override
 *
 * @param <T>
 */
@Component
public class GeneralController<T extends GeneralModel> implements ICRUDController<T> {

    protected final GeneralService<T> generalService;
    private final ObjectMapper mapper;

    private String[] requieredIDRoutes;

    @Setter
    private Class<?> aClass;

    public GeneralController(GeneralService<T> generalService) {
        this.generalService = generalService;
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.requieredIDRoutes = new String[]{
                RoutesConstants.CREATE_ROUTE,
                RoutesConstants.SAVEALL_ROUTE
        };
    }

    /**
     * Obtiene todos los registros para una entity T
     *
     * @return Lista con todos los registros
     * @throws Exception error en caso de fallo en la consulta
     */
    @Override
    public List<T> getAll(T t) throws Exception {
        return generalService.getAll(t);
    }

    /**
     * Obtiene el número total de elementos en la entidad.
     *
     * @return El número total de elementos.
     * @throws Exception Si ocurre algún error al obtener el recuento.
     */
    @Override
    public long getAllCount() throws Exception {
        return generalService.count();
    }

    /**
     * Obtiene un elemento de la entidad según el ID proporcionado.
     *
     * @param id El ID del elemento a obtener.
     * @return El elemento correspondiente al ID.
     * @throws Exception Si ocurre algún error al obtener el elemento.
     */
    @Override
    public T getByID(String id) throws Exception {
        return generalService.findById(id);
    }

    /**
     * Obtiene una lista de elementos de la entidad paginados según el número de página y el tamaño de página especificados.
     *
     * @param pageNumber El número de página.
     * @param pageSize   El tamaño de página.
     * @return Una lista de elementos paginados.
     * @throws Exception Si ocurre algún error al obtener la lista paginada.
     */
    @Override
    public List<T> getAll(int pageNumber, int pageSize) throws Exception {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return generalService.getAllPageable(page);
    }

    /**
     * Permite btener todos los registros segun el filtro aplicado
     *
     * @param t          el filtro a aplicar
     * @param pageNumber el nuimero de la pagina
     * @param pageSize   el tamaño de la pagina
     * @return los registros segun el filtro
     * @throws Exception en caso de no ser implementado
     */
    @Override
    public List<T> getAllByFilters(T t, int pageNumber, int pageSize) throws Exception {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return generalService.getAllByFilter(t, page);
    }

    /**
     * Cuenta los registros segun el filtro
     *
     * @param t el filtro a aplicar
     * @return el nuimero de registrso con ese filtro
     * @throws Exception en caso de no ser implementado
     */
    @Override
    public long countAllByFilters(T t) throws Exception {
        return generalService.countByFilter(t);
    }

    /**
     * Crea un nuevo elemento en la entidad.
     *
     * @param t El elemento a crear.
     * @return El elemento creado.
     * @throws Exception Si ocurre algún error al crear el elemento.
     */
    @Override
    public T create(T t) throws Exception {
        return generalService.save(t);
    }

    /**
     * Actualiza un elemento existente en la entidad.
     *
     * @param t El elemento a actualizar.
     * @return El elemento actualizado.
     * @throws Exception Si ocurre algún error al actualizar el elemento.
     */
    @Override
    public T update(T t) throws Exception {
        T tFound = generalService.findById(t.getId());
        return generalService.save(t);
    }

    /**
     * Elimina un elemento de la entidad según el ID proporcionado.
     *
     * @param id El ID del elemento a eliminar.
     * @return Un mapa con un mensaje de éxito si se elimina correctamente.
     * @throws Exception Si ocurre algún error al eliminar el elemento.
     */
    @Override
    public Map<String, String> delete(String id) throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", MessageConstants.FAILED_MESSAGE);

        try {
            generalService.delete(id);
            response.put("message", MessageConstants.SUCCESS_MESSAGE);
        } catch (Exception e) {
            throw new Exception(MessageConstants.FAILED_MESSAGE);
        }

        return response;
    }


    /**
     * Controlador de reigstro de multiples valores
     *
     * @param t lista de objetos a registrar
     * @return el estado de la respuesta del ingreso de multiples elementos
     * @throws Exception En caso de generar algun error en el multiple ingreso
     */
    @Override
    public Map<String, String> saveAll(List<T> t) throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", MessageConstants.FAILED_MESSAGE);

        try {
            generalService.saveAll(t);
            response.put("message", MessageConstants.SUCCESS_MESSAGE);
        } catch (Exception e) {
            throw new Exception(MessageConstants.FAILED_MESSAGE);
        }

        return response;
    }


    @Override
    public Map<String, String> deleteAll(String id) throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", MessageConstants.FAILED_MESSAGE);

        try {
            generalService.deleteAll(id);
            response.put("message", MessageConstants.SUCCESS_MESSAGE);
        } catch (Exception e) {
            throw new Exception(MessageConstants.FAILED_MESSAGE);
        }

        return response;
    }

    public ResponseEntity<?> handleRequest(HttpServletRequest request, List<?> dataList, int pageNumber, int pageSize, String id) {
        try {
            String operation = "/" + RequestUtil.getPartFromURI(request.getRequestURI(), 3);
            if (dataList == null) {
                dataList = new ArrayList<>();
            }
            List<? extends GeneralModel> parsedDataList = parseDataList(dataList, requireId(operation));
            return switch (operation) {
                case RoutesConstants.GET_ALL_BY_FILTER -> {
                    validateRequestMethod(request, HttpMethod.POST);
                    yield ResponseEntity.ok(getAll((T) parsedDataList.get(0)));
                }
                case RoutesConstants.GET_ALL_COUNT_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.GET);
                    yield ResponseEntity.ok(getAllCount());
                }
                case RoutesConstants.GET_BY_ID_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.GET);
                    yield ResponseEntity.ok(getByID(id));
                }
                case RoutesConstants.GET_ALL_BY_FILTERS_PAGED -> {
                    validateRequestMethod(request, HttpMethod.POST);
                    yield ResponseEntity.ok(getAllByFilters((T) parsedDataList.get(0), pageNumber, pageSize));
                }
                case RoutesConstants.COUNT_ALL_BY_FILTERS -> {
                    validateRequestMethod(request, HttpMethod.POST);
                    yield ResponseEntity.ok(countAllByFilters((T) parsedDataList.get(0)));
                }
                case RoutesConstants.CREATE_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.POST);
                    yield ResponseEntity.ok(create((T) parsedDataList.get(0)));
                }
                case RoutesConstants.UPDATE_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.PUT);
                    yield ResponseEntity.ok(update((T) parsedDataList.get(0)));
                }
                case RoutesConstants.DELETE_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.DELETE);
                    yield ResponseEntity.ok(delete(id));
                }
                case RoutesConstants.SAVEALL_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.POST);
                    yield ResponseEntity.ok(saveAll((List<T>) parsedDataList));
                }
                case RoutesConstants.DELETEALL_ROUTE -> {
                    validateRequestMethod(request, HttpMethod.DELETE);
                    yield ResponseEntity.ok(deleteAll(id));
                }
                default -> ResponseEntity.badRequest().body("Invalid operation");
            };
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private void validateRequestMethod(HttpServletRequest request, HttpMethod expectedMethod) {
        if (!request.getMethod().equals(expectedMethod.toString())) {
            throw new IllegalArgumentException("Invalid request method");
        }
    }

    private boolean requireId(String route) {
        for (String r : this.requieredIDRoutes) {
            if (route.equals(r)) {
                return true;
            }
        }
        return false;
    }

    private List<T> parseDataList(List<?> dataList, boolean requireId) {
        List<T> result = new ArrayList<>();

        for (Object data : dataList) {
            T instance = (T) this.mapper.convertValue(data, aClass);
            if (requireId) {
                if (instance.getId() == null) {
                    instance.setId(IdGeneratorUtil.generateUUID());
                }
            }
            result.add(instance);
        }

        return result;
    }


}
