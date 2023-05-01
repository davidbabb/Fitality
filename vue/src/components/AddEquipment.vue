<template>
    <div class="exercise">
    <div class="title">
        <h3>CREATE EQUIPMENT</h3>
      </div>
    <div class="input">
      <form @submit.prevent="submitForm">
        <div class="section">
          <label id="exercise-name">Exercise Name</label>
          <input v-model="newExercise.exerciseName" required />
        </div>
        <div class="section">
          <label>Equipment Name</label>
          <select v-model="newExercise.equipmentName" required>
            <option v-for="equipment in equipments" :key="equipment.equipmentId" :value="equipment.equipmentName">{{ equipment.equipmentName.toUpperCase() }}</option>
          </select>
        </div>  
          <button type="submit">Submit</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {

}
</script>

<style>

</style>