import React from 'react';
import Link from 'react-dom'
import axios from 'axios';

export default class Layouts extends React.Component {
  state = {
    layouts: []
  }

  componentDidMount() {
    axios.get(`https://10.42.0.1:4000/light/all`)
      .then(res => {
        console.log(res.body);
        const layouts = res.data;
        this.setState({ layouts });
      })
  }

  render() {
    return (
        <div>
        {this.state.layouts.map((e) => (
            <Link key={e.layout_name} to={this.setProfile(e.user.attributes.username)}>
              <button type="button">{e.user.attributes.username}</button>
            </Link>
          ))}
        </div>
    )
  }
}