<template>
  <div v-if="user.authorities[0].name === 'ROLE_ADMIN' && !workoutStarted"
         :class="{'start-workout-visible': !workoutStarted, 'end-workout-visible': workoutStarted}">
      <router-link :to="{name:'MachineMetrics'}" tag=button class="machine-metrics">
        Equipment Usage
        <img src="../img/machinemetricsimg.jpg" alt="machine metrics">
      </router-link>
    </div>
</template>

<script>
export default {

}
</script>

<style>

</style>