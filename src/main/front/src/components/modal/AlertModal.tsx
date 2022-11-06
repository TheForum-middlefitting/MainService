import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useDispatch, useSelector} from "react-redux";
import { alertActions} from "../../store/redux/alertSlice";

export default function AlertModal(props : any) {
    const dispatch = useDispatch();
    const show = useSelector((state : any) => state.alert.show);
    const message = useSelector((state : any) => state.alert.message);

    const handleClose = () => {
        dispatch(alertActions.close());
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
                    <Modal.Title>게시판의 내용</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {message}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleClose}>확인</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
