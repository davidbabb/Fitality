<template>
  <div class = "daily motivational">
    <div class = "motivational-title">
        <h2>Daily Motivation</h2>
    </div>
    <div class = "quote">
       <blockquote class = "body">
            "{{motivational.quote}}"
        </blockquote>
        <h4 class = "author">
            -&nbsp;{{motivational.author}}
        </h4>     
    </div>    
  </div>      
</template>

<script>
import motivationalService from "../services/MotivationalService";
export default {
    name: "Motivational",
    data() {
        return {
            motivational: ""
        }
    },
    created() {
        this.getMotivational();
    },
    methods: {
        getMotivational() {
            motivationalService.getRandomMotivational().then((response) => {
                this.motivational = response.data;
            });
        }
    }
}
</script>

<style>

</style>