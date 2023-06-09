<template>
  <div class="home">
    <h2>Welcome, {{ this.name }}!</h2>
    <div class="nav">
    </div>
    <div>
    </div>
    <button id="start-workout" v-if="!workoutStarted" @click="startWorkout" class="start-workout-btn">
      Start Workout
      <img src="../img/startimg.jpg" alt="start image">
    </button>
    <button id="end-workout" v-else @click="endWorkout" class="end-workout-btn">
      End Workout
      <img src="../img/endworkoutimg.jpg" alt="end image">
    </button>

    <div v-if="user" class="end-buttons">
      <div v-if="workoutStarted">
        <router-link :to="{name:'UserExercises', params: {userId: user.id}}" tag=button class="exercise-view-button">
        Add Exercises
        <img src="../img/viewexercisesimg.jpg" alt="view exercises">
      </router-link>
      </div>
      <div v-if="!workoutStarted">
        <router-link :to="{name:'UpcomingGymClassesView'}" tag=button class="upcoming-gym-classes">
            Upcoming Classes
            <img src="../img/upcomingclassimg.jpg" alt="upcoming classes">
        </router-link>
      </div>
    </div>
    <div v-if="user.authorities[0].name === 'ROLE_ADMIN' && !workoutStarted"
         :class="{'start-workout-visible': !workoutStarted, 'end-workout-visible': workoutStarted}">
      <router-link :to="{name:'EquipmentManagement'}" tag=button class="machine-metrics">
        Equipment Management
        <img src="../img/machinemetricsimg.jpg" alt="machine metrics" >
      </router-link>
    </div>
    <div  v-if="!workoutStarted">
      <Motivational/>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import WorkoutService from '../services/WorkoutService';
import AccountService from '../services/AccountService';
import Motivational from '../components/Motivational.vue';

export default {
  name: "home",

  data() {
    return {
      workoutStarted: false,
      latestWorkout: null,
      name: ""
    };
  },
  components: {
    Motivational
  },
  computed: {
    ...mapState(['user'])
  },
  created() {
    if (this.user.authorities[0].name == 'ROLE_USER') {
      AccountService.getNameByUserId(this.user.id).then(response => {
        this.name = response.data;
      });
    } else {
      this.name = this.user.username;
    }
  },
  methods: {
    async startWorkout() {
      this.workoutStarted = true;
      const currentTimestamp = new Date().toISOString().slice(0, 19).replace('T', ' ');
      const workout = {
        userId: this.user.id,
        timeOfEntry: currentTimestamp,
      };
      try {
        await WorkoutService.createWorkout(workout);
        console.log('Workout created');

        // Fetch the latest workout
        const response = await WorkoutService.getLatestWorkoutByUser(this.user.id);
        this.latestWorkout = response.data;
        console.log('Latest workout:', this.latestWorkout);
      } catch (error) {
        console.error('Error:', error);
      }
    },
    async endWorkout() {
      this.workoutStarted = false;
      
      if (this.latestWorkout) {
        const currentTime = new Date();
        const startTime = new Date(this.latestWorkout.timeOfEntry);
        const durationMs = currentTime - startTime;

        const durationHours = Math.floor(durationMs / (1000 * 60 * 60));
        const durationMinutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));
        const durationSeconds = Math.floor((durationMs % (1000 * 60)) / 1000);

        const duration = `${durationHours.toString().padStart(2, '0')}:${durationMinutes.toString().padStart(2, '0')}:${durationSeconds.toString().padStart(2, '0')}`;

        const currentDate = currentTime.toISOString().slice(0, 10);
        
        const workoutTimeData = {
          workoutId: this.latestWorkout.workoutId,
          date: currentDate,
          duration: duration,
        };

        try {
          await WorkoutService.createWorkoutTime(workoutTimeData);
          console.log('Workout time created');
        } catch (error) {
          console.error('Error creating workout time:', error);
        }
      }
    },
    refreshExercises() {
      this.$refs.userExercise.refreshExercises();
    },
  },
};
</script>
<style scoped>
.home {
  text-align: center;
  background-color: #e7e7e7 ;
  width: 100%;
  min-height: 100%;
  height: 100vh !important;
  box-sizing: border-box;
  padding-bottom: 75px;
}

button{
  background-color: #020002
; /* Green */
  border: none;
  border-radius: 5px;
  color: white;
  display: flex;
  flex-direction: row;
  padding: 70px 30px;
  text-align: left;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin-top: 10px;
  cursor: pointer;
  width: 95%;

}
button img {
  display: inline-block;
  position: absolute;
  width: 200px;
  height: 150px;
  top: 78px;
  right: 3vw;
  object-fit: cover;
}
.start-workout-btn{
  background-color: #020002
}

.end-buttons{
   
}

.exercise-view-button img{
  top: 246px;
  height: 155px;
}
.upcoming-gym-classes img{
  top: 245px;
  height: 155px;
}
.machine-metrics img{
  height: 155px;
}
.start-workout-visible .machine-metrics img {
  top: 247px;
}

.end-workout-visible .machine-metrics img {
  top: 515px;
}
.end-workout-btn img{
  height: 158px;
  width: 250px;
  top: 77px;
}
/* make a media with min width of 768 */
@media only screen and (min-width: 768px){
.home{
  padding-bottom: 0px;
  padding-top: 58px;
}

button img{
  top:136px;
  height: 155px;
}
.end-workout-btn img{
  height: 154px;
}
.exercise-view-button img{
  top: 307px
  
}
.upcoming-gym-classes img{
  top: 473px
}

.machine-metrics{
  margin-bottom: 20px;
}

.start-workout-visible .machine-metrics img {
  top: 305px;
}

.end-workout-visible .machine-metrics img {
 top: 572px;
}
.nav  {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
  background-color: white;
  width: 100%;
  height: 58px;
  overflow: hidden;

}
}
</style>

