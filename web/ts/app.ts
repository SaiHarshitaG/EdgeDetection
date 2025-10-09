const img = document.getElementById('img') as HTMLImageElement;
const fpsEl = document.getElementById('fps')!;
const resEl = document.getElementById('res')!;

// Sample base64 (1x1 white PNG placeholder).
const sampleBase64 = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR4nGNgYAAAAAMAAWgmWQ0AAAAASUVORK5CYII=';

let last = performance.now();
let frames = 0;

function tick() {
  const now = performance.now();
  frames++;
  if (now - last >= 1000) {
    (fpsEl as HTMLElement).innerText = (frames).toString();
    frames = 0;
    last = now;
  }
  requestAnimationFrame(tick);
}

window.onload = () => {
  img.src = sampleBase64;
  img.onload = () => { resEl.textContent = img.naturalWidth + 'x' + img.naturalHeight; };
  requestAnimationFrame(tick);
};
