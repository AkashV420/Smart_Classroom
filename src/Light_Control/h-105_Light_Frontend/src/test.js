import React, { Component } from "react";
import styled, { css } from "styled-components";
import MaterialFixedLabelTextbox from "./components/MaterialFixedLabelTextbox";

function Test(props) {
  return (
    <MaterialFixedLabelTextbox
      style={{
        width: 375,
        height: 43
      }}
      text1="Password"
      textInput1=""
      type='password'
    ></MaterialFixedLabelTextbox>
  );
}

export default Test;
