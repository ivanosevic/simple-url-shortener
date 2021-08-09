/**
 * Until document is ready...
 */
document.addEventListener("DOMContentLoaded", function (event) {
  const clipboard = new ClipboardJS('.copyable');

  clipboard.on('success', function () {
    alertify.notify('Copied! :)', 'success', 5);
  });

  clipboard.on('error', function () {
    alertify.notify('Error copying :(', 'error', 5);
  });

  const qrBtns = document.getElementsByClassName('qr-code');

  for (let i = 0; i < qrBtns.length; i++) {
    console.log("I'm setting up the button  = " + i);
    let qrBtn = qrBtns[i];
    qrBtn.addEventListener('click', evt => {
      evt.preventDefault();
      console.log("The even is firing!");
      let image = evt.target.getAttribute('qr-image');
      swal.fire({
        imageUrl: image,
        imageWidth: 400,
        imageHeight: 400,
        showConfirmButton: false,
        showCloseButton: true
      });
    });
  }
});