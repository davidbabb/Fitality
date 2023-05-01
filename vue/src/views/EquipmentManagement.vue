<template>
  <!-- <div>
      <router-link :to="{name:'MachineMetrics'}" tag=button class="machine-metrics">
        Equipment Usage
        <img src="../img/machinemetricsimg.jpg" alt="machine metrics">
      </router-link>
    </div> -->
    <div>
      <Machine-Metrics/>
    </div>
</template>

<script>
import MachineMetrics from '../views/MachineMetrics.vue';
export default {
  name: "EquipmentManagement",
  components: {
    MachineMetrics
  }
}
</script>

<style>

</style>