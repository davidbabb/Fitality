<template>
    <div>
      <Machine-Metrics/>
      <Add-Equipment/>
    </div>
</template>

<script>
import AddEquipment from '../components/AddEquipment.vue';
import MachineMetrics from '../views/MachineMetrics.vue';
export default {
  name: "EquipmentManagement",
  components: {
    MachineMetrics,
    AddEquipment
  }
}
</script>

<style>

</style>