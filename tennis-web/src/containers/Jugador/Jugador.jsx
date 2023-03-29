import React, { useEffect, useState } from 'react';
import Typography from '../../components/Typography/Typography';
import { Button } from 'react-bootstrap';
import TableJugadores from '../../components/Tables/TableJugadores';
import JugadorModal from '../../components/Modals/JugadorModal';
import httpClient from '../../lib/httpClient';

let jugadorInit = {
  nombre: '',
  puntos: 0,
  entrenador: {
    id: -1,
    nombre: '',
  }
};

const Jugador = (props) => {
  const [jugadoresList, setJugadoresList] = useState([]);
  const [jugadorData, setJugadorData] = useState(jugadorInit);
  const [isEdit, setIsEdit] = useState(false);
  const [hasErrorInForm, setHasErrorInForm] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [listaEntrenadores, setListaEntrenadores] = useState([]);

  useEffect(async () => {
    await getJugadores();
    await getEntrenadores();
  }, []);

  //Verbos
  const getJugadores = async () => {
    try {
      const data = await httpClient.get('/jugadores');
      setJugadoresList(data);
    } catch (error) {
      console.log(error);
    }
  };

  const getEntrenadores = async () => {
    try {
      const data = await httpClient.get('/entrenador');
      setListaEntrenadores(data);
    } catch (error) {
      console.log(error);
    }
  };

  const agregarJugador = async () => {
    try {
      const dataSend = { ...jugadorData };
      const data = await httpClient.post(`/jugadores`, { data: dataSend });
      setJugadoresList([...jugadoresList, data]);
    } catch (error) {
      console.log(error);
    }
    handleCloseModal();
  };

  const editarJugador = async (id) => {
    try {
      const data = await httpClient.put(`/jugadores/${id}`, { data: jugadorData });
      setJugadoresList(jugadoresList.map((item) => (item.id === id ? data : item)));
    } catch (error) {
      console.log(error);
    }
    handleCloseModal();
  };

  const borrarJugador = async (id) => {
    try {
      await httpClient.delete(`/jugadores/${id}`);
      setJugadoresList(jugadoresList.filter((jugador) => jugador.id !== id));
    } catch (error) {
      console.log(error);
    }
  };

  // Buttons
  const handleDetail = (data, event) => {
    event.preventDefault();
    props.history.push(`/jugador/detalle/${data.id}`, { data });
  };

  const handleEdit = (editData, event) => {
    event.preventDefault();
    handleOpenModal(true, editData);
  };
  const handleDelete = async (id, event) => {
    event.preventDefault();
    if (window.confirm('Estas seguro?')) {
      await borrarJugador(id);
    }
  };

  const handleRecalculateRanking = async (id, event) => {
    event.preventDefault();
    await httpClient.put(`/jugadores/${id}/actions/recalculateRanking`);
    const data = await httpClient.get(`/jugadores/${id}`);
    setJugadoresList(jugadoresList.map((item) => (item.id === id ? data : item)));
  };

  // Modal
  const handleOpenModal = (editarJugador = false, jugadorToEdit = null) => {
    setIsEdit(editarJugador);

    if (editarJugador) {
      setJugadorData(jugadorToEdit);
    }

    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setIsEdit(false);
    setHasErrorInForm(false);
    setJugadorData(jugadorInit);
    setErrorMsg('');
  };

  // Form

  const handleChangeInputForm = (property, value) => {
    value === '' ? setHasErrorInForm(true) : setHasErrorInForm(false);

    const newData = { ...jugadorData };

    switch (property) {
      case 'entrenador':
        newData.entrenador = listaEntrenadores.filter((x) => x.id === parseInt(value))[0];
        break;
      case 'nombre':
        newData.nombre = value
        break;
      case 'puntos':
        newData.puntos = value;
        break;
      default:
        break;
    }

    setJugadorData(newData);
  };

  // const handleChangeInputForm = (property, value) => {
  //   // Si el valor del input es vacío, entonces setea que hay un error
  //   value === '' ? setHasErrorInForm(true) : setHasErrorInForm(false);

  //   setJugadorData({ ...jugadorData, [property]: value });
  // };

  const handleSubmitForm = (e, form, isEdit) => {
    e.preventDefault();
    setHasErrorInForm(true);

    console.log("is edit", isEdit);

    if (form.checkValidity()) isEdit ? editarJugador(jugadorData.id) : agregarJugador();
  };

  // API

  return (
    <>
      <Typography id={'title-id'}>Jugador</Typography>

      <TableJugadores
        dataForTable={jugadoresList}
        detail={handleDetail}
        edit={handleEdit}
        delete={(id, event) => handleDelete(id, event)}
        recalcularRanking={handleRecalculateRanking}
      />
      <div className="mb-2" class="btn float-right">
        <Button variant="success" onClick={() => handleOpenModal()}>Agregar jugador</Button>
      </div>
      <JugadorModal
        show={openModal}
        onHide={handleCloseModal}
        isEdit={isEdit}
        handleChange={handleChangeInputForm}
        jugador={jugadorData}
        validated={hasErrorInForm}
        handleSubmit={handleSubmitForm}
        errorMsg={errorMsg}
        listaEntrenadores={listaEntrenadores}
      />
    </>
  );
};

export default Jugador;
