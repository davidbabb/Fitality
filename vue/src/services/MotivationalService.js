import axios from 'axios';

export default {
    getRandomMotivational() {
        return axios.get('/motivationals/random');
    }
}
