import React, { Component } from "react";
import styled, { css } from "styled-components";

function MaterialFixedLabelTextbox(props) {
  return (
    <Container {...props}>
      <Label>{props.text1 || "FixedLabel"}</Label>
      <InputStyle placeholder={props.textInput1 || undefined}></InputStyle>
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  background-color: transparent;
  flex-direction: row;
  padding-left: 16px;
  border-color: #D9D5DC;
  border-bottom-width: 1px;
`;

const Label = styled.span`
  font-family: Roboto;
  color: #000;
  align-self: flex-start;
  opacity: 0.5;
  padding-top: 16px;
  padding-bottom: 8px;
  font-size: 16px;
  line-height: 16px;
`;

const InputStyle = styled.input`
  font-family: Roboto;
  flex: 1 1 0%;
  color: #000;
  align-self: stretch;
  padding-top: 14px;
  padding-right: 5px;
  padding-bottom: 8px;
  padding-left: 30px;
  font-size: 16px;
  line-height: 16px;
  border: none;
  background: transparent;
  display: flex;
  flex-direction: column;
`;

export default MaterialFixedLabelTextbox;
