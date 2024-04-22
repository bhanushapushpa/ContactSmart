console.log("asdk");



const toggleSideBar = () => {
	if ($(".sideBar").is(":visible")) {
		//close sidebar
		$(".sideBar").css("display", "none");
		$(".content").css("margin-left", "2%");
		$(".content").css("width", "100%");
	}
	else {
		//open sidebar
		$(".sideBar").css("display", "block");
		$(".content").css("margin-left", "23%");
	}
}

const deleteContacts = (cid) => {

	Swal.fire({
		title: "Are you sure?",
		text: "Once deleted, you will not be able to recover this contact !!!",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#3085d6",
		cancelButtonColor: "#d33",
		confirmButtonText: "Yes, delete it!"
	}).then((result) => {
		if (result.isConfirmed) {
			window.location = '/user/deleteContact/' + cid;
		}
	});
}


const search = () =>{

    let query= $("#search-input").val();

    if(query == ""){
        $(".search-result").hide();
    }
    else{
        let url =`http://localhost:8080/search/${query}`;

        fetch(url).then(response => {
            return response.json();
        }).then(data =>{
            let text = `<div class='list-group '>`;

            data.forEach(contact => {
                text += `<a href='/user/${contact.contact_Id}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`;
            })

            text += `</div>`;
            $(".search-result").html(text);
            $(".search-result").show();
        });

        
    }

}


