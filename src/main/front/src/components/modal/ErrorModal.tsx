import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useDispatch, useSelector} from "react-redux";
import { errorActions} from "../../store/redux/errorSlice";

export default function ErrorModal(props : any) {
    const dispatch = useDispatch();
    const show = useSelector((state : any) => state.error.show);
    const message = useSelector((state : any) => state.error.message);
    const code = useSelector((state : any) => state.error.code);
    const status = useSelector((state : any) => state.error.status);

    const handleClose = () => {
        dispatch(errorActions.close());
    };

    return (
        <>
            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header closeButton>
                    <Modal.Title>ERROR {status} {code}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {message}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="outline-danger" onClick={handleClose}>확인</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
