import axios from 'axios';

export default {
  getEquipment() {
    return axios.get('/equipments');
  },
  getMachineMetrics(equipmentUsageDateTime){
    return axios.get(`/equipments/usage/${equipmentUsageDateTime}`);
  },
  getMachineMetricsByName(equipmentUsageDateTime, equipmentName) {
    return axios.get(`/equipments/usage/${equipmentUsageDateTime}/${equipmentName}`);
  },
  getYears() {
    return axios.get('/equipments/years');
  },
  getEquipmentNameByExercise(exerciseName) {
    const num = exerciseName.indexOf(' ');
    const str = exerciseName.slice(0, num) + '%20' + exerciseName.slice(num+1, exerciseName.length);
    return axios.get(`equipments/name/${str}`);
  }
};
