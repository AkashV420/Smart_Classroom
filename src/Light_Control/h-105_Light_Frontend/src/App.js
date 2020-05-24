import React, { Component } from 'react';
import Link from 'react-dom';
import logo from './logo.svg';
import './App.css';
import Layout from './Components/Layouts'
import Test from './test';
import { render } from '@testing-library/react';
import MaterialButtonPrimary1 from './components/MaterialButtonPrimary1';

import axios from 'axios';

function importAll(r) {
  return r.keys().map(r);
}



export default class App extends Component{
  constructor(props){
    super(props);
    const images = importAll(require.context('./assets/img/', false, /\.(png|jpe?g|svg)$/));

    this.state={
      json_object:[],
      classroom: {},
      imgsrc:require('./assets/img/0000000000000000000.png')
    };  
  }
  
  componentDidMount(){
    axios.get(`http://localhost:4000/light/all`)
    .then((response) => {
      console.log(response.data);
      // alert(response.data);
      this.setState({json_object:response.data});

    }, (error) => {
      console.log(error);
    });
    axios.get(`http://localhost:4000/light/current_config`)
    .then((response) => {
      console.log(response.data);
      // alert(response.data);
      this.setState({classroom:response.data[0]});
    }, (error) => {
      console.log(error);
    });
  }
  
  clicked=(obj)=>{
    // alert(obj);
    axios.post(`http://localhost:4000/light/activate`,{classroom_name:"h-105", current_light_configuration:obj.light_configuration})
    .then((response) => {
      console.log(response);
    }, (error) => {
      console.log('nhi hua kuch');
      console.log(error);
    });
    this.setState({imgsrc:require('./assets/img/'+obj.light_configuration+'.png')});
    // alert(this.state.imgsrc);
  }
  render(){
  return (
    <div className="App">
      <header className="App-header">
        <div class="split left">
          <div class="centered">
              <h2>
                {this.state.classroom.classroom_name}
              </h2>
              {this.state.json_object.map(obj=><button clicked='false' onClick={()=>this.clicked(obj)} style={{
              width: 200,
              height: 50,
              padding:10,
              color: 'blue',
              fontSize:20,
              marginBottom:5
            }}>{obj.layout_name}</button>
      )}
            
          </div>
        </div>

        <div class="split right">
          <div class="centered">
            {/* <img src={require(this.state.imgsrc)} alt="Avatar man"></img> */}
            <img src={this.state.imgsrc} alt="Avatar man"></img>
            {/* <h2>John Doe</h2> */}
            <p>Current Light Configuration</p>
          </div>
        </div>
      </header>
    </div>
  );
  }
}

