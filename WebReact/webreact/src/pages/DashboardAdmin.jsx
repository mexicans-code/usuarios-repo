import React, { useState, useEffect } from 'react';
import axios from 'axios';

export default function DashboardAdmin() {
    const [divisiones, setDivisiones] = useState([]);
    const [programas, setProgramas] = useState([]);
    const [showModalDivision, setShowModalDivision] = useState(false);
    const [showModalPrograma, setShowModalPrograma] = useState(false);
    const [formDataDivision, setFormDataDivision] = useState({
        nombre: '',
        activo: true,
        image: ''
    });
    const [formDataPrograma, setFormDataPrograma] = useState({
        nombre: '',
        activo: true,
        divisionId: ''
    });
    const [editMode, setEditMode] = useState(false);
    const [selectedId, setSelectedId] = useState(null);

    useEffect(() => {
        fetchDivisiones();
        fetchProgramas();
    }, []);

    const fetchDivisiones = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/divisiones');
            console.log('Divisiones cargadas:', response.data);
            setDivisiones(response.data);
        } catch (error) {
            console.error('Error al cargar divisiones:', error);
            alert('Error al cargar las divisiones');
        }
    };

    const fetchProgramas = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/programas');
            console.log('Programas cargados:', response.data);
            setProgramas(response.data);
        } catch (error) {
            console.error('Error al cargar programas:', error);
            alert('Error al cargar los programas educativos');
        }
    };

    const handleCreateDivision = async (e) => {
        e.preventDefault();
        
        try {
            const divisionData = {
                nombre: formDataDivision.nombre,
                activo: formDataDivision.activo,
                image: formDataDivision.image
            };

            if (editMode) {
                await axios.put(`http://localhost:8080/api/divisiones/${selectedId}`, divisionData);
                alert('División actualizada exitosamente');
            } else {
                await axios.post('http://localhost:8080/api/divisiones', divisionData);
                alert('División creada exitosamente');
            }

            setFormDataDivision({ nombre: '', activo: true, image: '' });
            setShowModalDivision(false);
            setEditMode(false);
            setSelectedId(null);
            fetchDivisiones();
        } catch (error) {
            console.error('Error al guardar división:', error);
            alert(error.response?.data?.message || 'Error al guardar la división');
        }
    };

    const handleCreatePrograma = async (e) => {
        e.preventDefault();
        
        if (!formDataPrograma.divisionId || formDataPrograma.divisionId === '') {
            alert('Por favor seleccione una división');
            return;
        }

        if (!formDataPrograma.nombre || formDataPrograma.nombre.trim() === '') {
            alert('Por favor ingrese un nombre para el programa');
            return;
        }

        try {
            const programaData = {
                programa: formDataPrograma.nombre.trim(),
                activo: formDataPrograma.activo,
                divisionId: Number(formDataPrograma.divisionId)
            };

            console.log('Enviando datos:', programaData);

            if (editMode) {
                await axios.put(`http://localhost:8080/api/programas/${selectedId}`, programaData);
                alert('Programa actualizado exitosamente');
            } else {
                const response = await axios.post('http://localhost:8080/api/programas', programaData);
                console.log('Respuesta del servidor:', response.data);
                alert('Programa creado exitosamente');
            }

            setFormDataPrograma({ nombre: '', activo: true, divisionId: '' });
            setShowModalPrograma(false);
            setEditMode(false);
            setSelectedId(null);
            fetchProgramas();
        } catch (error) {
            console.error('Error completo:', error);
            alert(error.response?.data?.message || 'Error al guardar el programa');
        }
    };

    const handleEditDivision = (division) => {
        setFormDataDivision({
            nombre: division.nombre,
            activo: division.activo,
            image: division.image || ''
        });
        setSelectedId(division.divisionId);
        setEditMode(true);
        setShowModalDivision(true);
    };

    const handleEditPrograma = (programa) => {
        setFormDataPrograma({
            nombre: programa.programa,
            activo: programa.activo,
            divisionId: programa.divisionId || ''
        });
        setSelectedId(programa.programaId);
        setEditMode(true);
        setShowModalPrograma(true);
    };
    const handleDeleteDivision = async (id) => {
        const programasAsociados = programas.filter(p => p.divisionId === id);
        
        if (programasAsociados.length > 0) {
            alert(`No se puede eliminar la división porque tiene ${programasAsociados.length} programa(s) educativo(s) asociado(s). Elimine primero los programas.`);
            return;
        }
        
        if (window.confirm('¿Está seguro de eliminar esta división?')) {
            try {
                await axios.delete(`http://localhost:8080/api/divisiones/${id}`);
                alert('División eliminada exitosamente');
                fetchDivisiones();
            } catch (error) {
                console.error('Error al eliminar división:', error);
                alert('Error al eliminar la división');
            }
        }
    };

    const handleDeletePrograma = async (id) => {
        if (window.confirm('¿Está seguro de eliminar este programa?')) {
            try {
                await axios.delete(`http://localhost:8080/api/programas/${id}`);
                alert('Programa eliminado exitosamente');
                fetchProgramas();
            } catch (error) {
                console.error('Error al eliminar programa:', error);
                alert('Error al eliminar el programa');
            }
        }
    };

    const handleToggleActivoPrograma = async (id, currentState) => {
        try {
            const endpoint = currentState 
                ? `http://localhost:8080/api/programas/${id}/deshabilitar`
                : `http://localhost:8080/api/programas/${id}/habilitar`;
            
            await axios.patch(endpoint);
            fetchProgramas();
        } catch (error) {
            console.error('Error al cambiar estado:', error);
            alert('Error al cambiar el estado del programa');
        }
    };

    const handleInputChangeDivision = (e) => {
        const { name, value, type, checked } = e.target;
        setFormDataDivision({
            ...formDataDivision,
            [name]: type === 'checkbox' ? checked : value
        });
    };

    const handleInputChangePrograma = (e) => {
        const { name, value, type, checked } = e.target;
        setFormDataPrograma({
            ...formDataPrograma,
            [name]: type === 'checkbox' ? checked : value
        });
    };

    const getDivisionNombre = (divisionId) => {
        const division = divisiones.find(d => d.divisionId === divisionId);
        return division ? division.nombre : 'N/A';
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Dashboard Administrativo</h1>

            <div className="card mb-4">
                <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <h3 className="mb-0">Divisiones</h3>
                    <button 
                        className="btn btn-light btn-sm" 
                        onClick={() => {
                            setFormDataDivision({ nombre: '', activo: true, image: '' });
                            setEditMode(false);
                            setShowModalDivision(true);
                        }}
                    >
                        + Crear División
                    </button>
                </div>
                <div className="card-body">
                    <div className="table-responsive">
                        <table className="table table-striped table-bordered">
                            <thead className="table-primary">
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Imagen</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {divisiones.length === 0 ? (
                                    <tr>
                                        <td colSpan="5" className="text-center">
                                            No hay divisiones registradas
                                        </td>
                                    </tr>
                                ) : (
                                    divisiones.map((division) => (
                                        <tr key={division.divisionId}>
                                            <td>{division.divisionId}</td>
                                            <td>{division.nombre}</td>
                                            <td>
                                                {division.image ? (
                                                    <img src={division.image} alt={division.nombre} style={{width: '50px', height: '50px', objectFit: 'cover'}} />
                                                ) : 'Sin imagen'}
                                            </td>
                                            <td>
                                                <span className={`badge ${division.activo ? 'bg-success' : 'bg-secondary'}`}>
                                                    {division.activo ? 'Activo' : 'Inactivo'}
                                                </span>
                                            </td>
                                            <td>
                                                <button 
                                                    className="btn btn-warning btn-sm me-2"
                                                    onClick={() => handleEditDivision(division)}
                                                >
                                                    Editar
                                                </button>
                                                <button 
                                                    className="btn btn-danger btn-sm"
                                                    onClick={() => handleDeleteDivision(division.divisionId)}
                                                >
                                                    Eliminar
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div className="card mb-4">
                <div className="card-header bg-success text-white d-flex justify-content-between align-items-center">
                    <h3 className="mb-0">Programas Educativos</h3>
                    <button 
                        className="btn btn-light btn-sm" 
                        onClick={() => {
                            setFormDataPrograma({ nombre: '', activo: true, divisionId: '' });
                            setEditMode(false);
                            setShowModalPrograma(true);
                        }}
                    >
                        + Crear Programa
                    </button>
                </div>
                <div className="card-body">
                    <div className="table-responsive">
                        <table className="table table-striped table-bordered">
                            <thead className="table-success">
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>División</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                {programas.length === 0 ? (
                                    <tr>
                                        <td colSpan="5" className="text-center">
                                            No hay programas educativos registrados
                                        </td>
                                    </tr>
                                ) : (
                                    programas.map((programa) => (
                                        <tr key={programa.programaId}>
                                            <td>{programa.programaId}</td>
                                            <td>{programa.programa}</td>
                                            <td>{getDivisionNombre(programa.divisionId)}</td>
                                            <td>
                                                <span className={`badge ${programa.activo ? 'bg-success' : 'bg-secondary'}`}>
                                                    {programa.activo ? 'Activo' : 'Inactivo'}
                                                </span>
                                            </td>
                                            <td>
                                                <button 
                                                    className="btn btn-warning btn-sm me-2"
                                                    onClick={() => handleEditPrograma(programa)}
                                                >
                                                    Editar
                                                </button>
                                                <button 
                                                    className="btn btn-danger btn-sm"
                                                    onClick={() => handleDeletePrograma(programa.programaId)}
                                                >
                                                    Eliminar
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            {showModalDivision && (
                <div className="modal show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog modal-lg modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">
                                    {editMode ? 'Editar División' : 'Crear División'}
                                </h5>
                                <button 
                                    type="button" 
                                    className="btn-close" 
                                    onClick={() => setShowModalDivision(false)}
                                ></button>
                            </div>
                            <div className="modal-body">
                                <form onSubmit={handleCreateDivision} id="divisionForm">
                                    <div className="mb-3">
                                        <label htmlFor="nombre" className="form-label">Nombre *</label>
                                        <input 
                                            type="text" 
                                            className="form-control" 
                                            id="nombre" 
                                            name="nombre"
                                            value={formDataDivision.nombre}
                                            onChange={handleInputChangeDivision}
                                            required 
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="image" className="form-label">URL de Imagen</label>
                                        <input 
                                            type="text" 
                                            className="form-control" 
                                            id="image" 
                                            name="image"
                                            value={formDataDivision.image}
                                            onChange={handleInputChangeDivision}
                                        />
                                    </div>
                                    <div className="mb-3 form-check">
                                        <input 
                                            type="checkbox" 
                                            className="form-check-input" 
                                            id="activo" 
                                            name="activo"
                                            checked={formDataDivision.activo}
                                            onChange={handleInputChangeDivision}
                                        />
                                        <label className="form-check-label" htmlFor="activo">Activo</label>
                                    </div>
                                </form>
                            </div>
                            <div className="modal-footer">
                                <button 
                                    type="button" 
                                    className="btn btn-secondary" 
                                    onClick={() => setShowModalDivision(false)}
                                >
                                    Cancelar
                                </button>
                                <button 
                                    type="submit" 
                                    className="btn btn-primary"
                                    form="divisionForm"
                                >
                                    {editMode ? 'Actualizar' : 'Crear'}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {showModalPrograma && (
                <div className="modal show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
                    <div className="modal-dialog modal-lg modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">
                                    {editMode ? 'Editar Programa Educativo' : 'Crear Programa Educativo'}
                                </h5>
                                <button 
                                    type="button" 
                                    className="btn-close" 
                                    onClick={() => setShowModalPrograma(false)}
                                ></button>
                            </div>
                            <div className="modal-body">
                                <form onSubmit={handleCreatePrograma} id="programaForm">
                                    <div className="mb-3">
                                        <label htmlFor="nombrePrograma" className="form-label">Nombre *</label>
                                        <input 
                                            type="text" 
                                            className="form-control" 
                                            id="nombrePrograma" 
                                            name="nombre"
                                            value={formDataPrograma.nombre}
                                            onChange={handleInputChangePrograma}
                                            required 
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="divisionId" className="form-label">División *</label>
                                        <select 
                                            className="form-select" 
                                            id="divisionId" 
                                            name="divisionId"
                                            value={formDataPrograma.divisionId}
                                            onChange={handleInputChangePrograma}
                                            required
                                        >
                                            <option value="">Seleccione una división</option>
                                            {divisiones.filter(d => d.activo).map(division => (
                                                <option key={division.divisionId} value={division.divisionId}>
                                                    {division.nombre}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="mb-3 form-check">
                                        <input 
                                            type="checkbox" 
                                            className="form-check-input" 
                                            id="activoPrograma" 
                                            name="activo"
                                            checked={formDataPrograma.activo}
                                            onChange={handleInputChangePrograma}
                                        />
                                        <label className="form-check-label" htmlFor="activoPrograma">Activo</label>
                                    </div>
                                </form>
                            </div>
                            <div className="modal-footer">
                                <button 
                                    type="button" 
                                    className="btn btn-secondary" 
                                    onClick={() => setShowModalPrograma(false)}
                                >
                                    Cancelar
                                </button>
                                <button 
                                    type="submit" 
                                    className="btn btn-success"
                                    form="programaForm"
                                >
                                    {editMode ? 'Actualizar' : 'Crear'}
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}