$(":checkbox[name='4[]']").change(function(){
  if ($(":checkbox[name='4[]']:checked").length == 1)  {
   $(':checkbox:not(:checked)').prop('disabled', true);
   $(":checkbox").each(function() {
            if(this.checked){
                document.getElementById(this.id+"tx").disabled = false;

            } else {
                document.getElementById(this.id+"tx").disabled = true;
            }
        });}
  else  {
   $(':checkbox:not(:checked)').prop('disabled', false);
   $(":checkbox").each(function() {
   document.getElementById(this.id+"tx").disabled = true;
   document.getElementById("fr").reset();
   });
  }
});