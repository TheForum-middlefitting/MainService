import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import {useDispatch, useSelector} from "react-redux";
import { warningActions} from "../../store/redux/warningSlice";

export default function WarningModal(props : any) {
    const dispatch = useDispatch();
    const show = useSelector((state : any) => state.warning.show);
    const message = useSelector((state : any) => state.warning.message);
    const executeFunction = useSelector((state : any) => state.warning.executeFunction);

    const handleClose = () => {
        dispatch(warningActions.close());
    };

    const handlerExecute = () => {
        executeFunction();
        handleClose();
    }

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
                    <Button variant="secondary" onClick={handleClose}>
                        취소
                    </Button>
                    <Button variant="primary" onClick={handlerExecute}>확인</Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
