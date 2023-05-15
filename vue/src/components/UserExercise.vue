<template>
  <div class="update">
    <div class="update-title">
      <h3>YOUR EXERCISES</h3>
    </div>
    <div class="form">
      <form @submit.prevent="submitForm" >
        <div class="box">
          <label> Select a Date </label>
          <input type="date" v-model="checkIn"/>
          <button type="submit"> Generate Report </button>
        </div>
        
        
      </form>
    </div>
    
    <div v-if="exercises.length > 0" class="list">
      <ul>
        <li v-for="exercise in exercises" :key="exercise.id">
          <div>Equipment Name: {{ exercise.equipmentName }}</div>
          <div>Exercise Name: {{ exercise.exerciseName }}</div>
          <div v-if="exercise.set > 0">Set: {{ exercise.set }}</div>
          <div v-if="exercise.rep > 0">Rep: {{ exercise.rep }}</div>
          <div v-if="exercise.weightLifted > 0">Weight Lifted: {{ exercise.weightLifted }}</div>
          <div v-if="exercise.singleWorkoutDuration > 0">Single Workout Duration: {{ exercise.singleWorkoutDuration }}</div>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>You don't have any exercises.</p>
    </div>
  </div>
</template>

<script>
import exerciseService from "../services/ExerciseService";
export default {
  name: "UserExercise",
  props: {
    userId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      exercises: [],
      checkIn: ""
    };
  },
  created() {
    this.refreshExercises();
  },
  methods: {
    refreshExercises() {
      exerciseService.getExercise(this.userId).then((response) => {
        this.exercises = response.data;
      });
    },
    submitForm() {
      exerciseService.getExercisesByDateAndUserId(this.checkIn, this.userId).then(response => {
        this.exercises = response.data;
        console.log("work");
      })
    }
  },
};
</script>

<style scoped>

/* Your Exercises */
.update{
  background-color: #e7e6e9;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  
}
.update-title {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  margin-bottom:10px;
  width: 100%;
  z-index: 1;
}
form{
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 95%;
  margin-bottom: 30px;
}
.form{
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}
.box{
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  margin-bottom: 40px;
  border-radius: 5px;
  width: 100%;
}
button{
  position: absolute;
  background-color: red;
  border-radius: 20px;
  width: 200px;
  padding: 15px;
  border:none;
  color: white;
  font-size: large;
  margin: 10px 0px;
  right: 20px;
  top:127px;
}
input{
  width: 60%;
  border-radius: 5px;
  border: 1px solid #ccc;
  padding: 10px;
  box-sizing: border-box;
}
.list{
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 95%;
  border-radius: 5px;
  margin-bottom:10px;
}
ul{
  list-style-type: none;
  width: 100%;
  padding: 0;
  margin: 0;
}
li{
  background-color: white;
  border-radius: 5px;
  margin-bottom: 10px;
  width: 100%;
  padding-top: 10px;
  padding-bottom: 10px;
}
li div{
  
  padding-left: 20px;
}
label{
  padding: 20px;
}
input{
  margin-right:20px;
}

@media screen and (min-width: 768px) {
  .update-title{
    justify-content: start;
    position: fixed;
  }
  h3{
    padding-left: 20px;
  }
  .list{
    margin-top: 0px;
  }
  form{
    margin-top: 70px;
  }
}

</style>
