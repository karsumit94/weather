import React, {Component} from 'react';

class Weather extends Component {

    constructor() {
        super();
        this.state = {
            initial: 'state',
            city: []
        }
        this.state = {};

        this.handleClick = this.handleClick.bind(this);
        this.updateCity = this.updateCity.bind(this);
        this.getDate = this.getDate.bind(this);

        fetch('/api/v1/cities').then(response => {
            if(response.ok) return response.json();
            throw new Error('Request failed.');
        }).then(data => {
            console.log(data);
            this.options = data;
            this.setState({cityList:data});
        }).catch(error => {
            console.log(error);
        });
    }
    handleClick() {
        console.log(this.state.cityInput);
        fetch('api/v1/weather?city='+this.state.cityInput).then(response => {
            if(response.ok) return response.json();
            throw new Error('Request failed.');
        }).then(data => {
            this.options = data;
            this.setState({weather:data});
        }).catch(error => {
            console.log(error);
        });
    }
    updateCity (event) {
        this.setState(prevState => ({
            cityInput: event.target.value
        }));
    }
    getDate(){
        let d = new Date();
        let mm = d.getMonth() + 1;
        let dd = d.getDate();
        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;
        return (dd + '-' + mm)
    }
    render() {
        return (
            <div>
                <input onChange={event => this.updateCity(event)}></input>
                <button onClick={this.handleClick}>
                    Get Weather Data
                </button>
                <div>
                    <p>{this.state.weather==""?"":JSON.stringify(this.state.weather)}</p>
                </div>
            </div>)
    }

}
export default Weather;